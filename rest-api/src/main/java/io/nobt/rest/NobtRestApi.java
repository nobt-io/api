package io.nobt.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.nobt.application.BodyParser;
import io.nobt.application.NobtApplication;
import io.nobt.core.ConversionInformationInconsistentException;
import io.nobt.core.NobtCalculator;
import io.nobt.core.UnknownNobtException;
import io.nobt.core.domain.Nobt;
import io.nobt.core.domain.NobtFactory;
import io.nobt.core.domain.NobtId;
import io.nobt.core.domain.Transaction;
import io.nobt.persistence.NobtRepository;
import io.nobt.persistence.NobtRepositoryCommand;
import io.nobt.persistence.NobtRepositoryCommandInvoker;
import io.nobt.rest.payloads.CreateExpenseInput;
import io.nobt.rest.payloads.CreateNobtInput;
import io.nobt.rest.payloads.NobtResource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import spark.ExceptionHandler;
import spark.Request;
import spark.Response;
import spark.Service;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.HashSet;
import java.util.Set;

import static java.util.Collections.emptySet;

public class NobtRestApi {

    private static final Logger LOGGER = LogManager.getLogger(NobtRestApi.class);

    private final Service http;
    private final NobtRepositoryCommandInvoker nobtRepositoryCommandInvoker;
    private final NobtCalculator nobtCalculator;
    private final BodyParser bodyParser;
    private final ObjectMapper objectMapper;
    private final NobtFactory nobtFactory;

    public NobtRestApi(Service service, NobtRepositoryCommandInvoker nobtRepositoryCommandInvoker, NobtCalculator nobtCalculator, BodyParser bodyParser, ObjectMapper objectMapper, NobtFactory nobtFactory) {
        this.http = service;
        this.nobtRepositoryCommandInvoker = nobtRepositoryCommandInvoker;
        this.nobtCalculator = nobtCalculator;
        this.bodyParser = bodyParser;
        this.objectMapper = objectMapper;
        this.nobtFactory = nobtFactory;
    }

    public void run(int port) {
        http.port(port);

        setupCORS();

        registerApplicationRoutes();
        registerTestFailRoute();

        http.exception(UnknownNobtException.class, (e, request, response) -> {
            response.status(404);
            response.body(e.getMessage());
        });

        http.exception(ConversionInformationInconsistentException.class, (e, request, response) -> {
            response.status(400);
            response.body(e.getMessage());
        });

        registerValidationErrorExceptionHandler();
        registerUncaughtExceptionHandler();
    }

    private void registerApplicationRoutes() {
        registerCreateNobtRoute();
        registerRetrieveNobtRoute();
        registerCreateExpenseRoute();
        registerDeleteExpenseRoute();
    }

    private void registerCreateExpenseRoute() {
        http.post("/nobts/:nobtId/expenses", "application/json", (req, resp) -> {

            final NobtId databaseId = decodeNobtIdentifierToDatabaseId(req);
            final CreateExpenseInput input = bodyParser.parseBodyAs(req, CreateExpenseInput.class);


            nobtRepositoryCommandInvoker.invoke(new NobtRepositoryCommand<Void>() {
                @Override
                public Void execute(NobtRepository repository) {

                    final Nobt nobt = repository.getById(databaseId);
                    nobt.addExpense(input.name, input.splitStrategy, input.debtee, new HashSet<>(input.shares), input.date, input.conversionInformation);
                    repository.save(nobt);

                    return null;
                }
            });

            // TODO return id of expense
            resp.status(201);

            return "";
        });
    }

    private void registerDeleteExpenseRoute() {

        http.delete("/nobts/:nobtId/expenses/:expenseId", (req, res) -> {

            final NobtId databaseId = decodeNobtIdentifierToDatabaseId(req);
            final Long expenseId = Long.parseLong(req.params(":expenseId"));


            nobtRepositoryCommandInvoker.invoke(new NobtRepositoryCommand<Void>() {
                @Override
                public Void execute(NobtRepository repository) {

                    final Nobt nobt = repository.getById(databaseId);
                    nobt.removeExpense(expenseId);
                    repository.save(nobt);

                    return null;
                }
            });


            res.status(204);

            return "";
        });
    }

    private void registerRetrieveNobtRoute() {
        http.get("/nobts/:nobtId", (req, res) -> {

            final NobtId databaseId = decodeNobtIdentifierToDatabaseId(req);


            final Nobt nobt = nobtRepositoryCommandInvoker.invoke(repository -> repository.getById(databaseId));
            final Set<Transaction> transactions = nobtCalculator.calculate(nobt);


            res.header("Content-Type", "application/json");

            return new NobtResource(nobt, transactions);
        }, objectMapper::writeValueAsString);
    }

    private void registerCreateNobtRoute() {
        http.post("/nobts", "application/json", (req, res) -> {

            final CreateNobtInput input = bodyParser.parseBodyAs(req, CreateNobtInput.class);


            final Nobt nobt = nobtRepositoryCommandInvoker.invoke(repository -> {

                final Nobt unpersistedNobt = nobtFactory.create(input.nobtName, input.explicitParticipants, input.currencyKey);
                final NobtId id = repository.save(unpersistedNobt);

                return repository.getById(id);
            });


            res.status(201);
            res.header("Location", req.url() + "/" + nobt.getId().toExternalIdentifier());
            res.header("Content-Type", "application/json");

            return new NobtResource(nobt, emptySet());
        }, objectMapper::writeValueAsString);
    }

    private void registerTestFailRoute() {
        http.get("/fail", (req, res) -> {
            throw new RuntimeException("This should go wrong.");
        });
    }

    private static NobtId decodeNobtIdentifierToDatabaseId(Request req) {
        final String externalIdentifier = req.params(":nobtId");
        return NobtId.fromExternalIdentifier(externalIdentifier);
    }

    private void setupCORS() {
        http.before((req, res) -> {
            res.header("Access-Control-Allow-Origin", "*");
            res.header("Access-Control-Request-Method", "*");
            res.header("Access-Control-Allow-Headers", "*");
        });

        http.options("/*", (req, res) -> {
            String accessControlRequestHeaders = req.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                res.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }

            String accessControlRequestMethod = req.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                res.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }
            return "OK";
        });
    }

    private void registerValidationErrorExceptionHandler() {
        http.exception(ConstraintViolationException.class, (ve, request, response) -> {

            final Set<ConstraintViolation<?>> violations = ((ConstraintViolationException) ve).getConstraintViolations();

            response.header("Content-Type", "application/problem+json");
            response.status(400);

            try {
                response.body(objectMapper.writeValueAsString(new ValidationProblem(violations)));
            } catch (JsonProcessingException e) {
                throw new IllegalStateException(e);
            }
        });
    }

    private void registerUncaughtExceptionHandler() {
        http.exception(Exception.class, new UncaughtExceptionHandler());
        http.exception(RuntimeException.class, new UncaughtExceptionHandler());
    }

    private static class UncaughtExceptionHandler implements ExceptionHandler {

        @Override
        public void handle(Exception e, Request request, Response response) {

            LOGGER.error(NobtApplication.SENTRY, "Unhandled exception.", e);

            response.status(500);
            response.body("");
        }
    }
}
