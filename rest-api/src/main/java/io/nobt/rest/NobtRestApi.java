package io.nobt.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.nobt.application.BodyParser;
import io.nobt.application.NobtApplication;
import io.nobt.core.ConversionInformationInconsistentException;
import io.nobt.core.UnknownNobtException;
import io.nobt.core.commands.CreateExpenseCommand;
import io.nobt.core.commands.CreatePaymentCommand;
import io.nobt.core.commands.DeleteExpenseCommand;
import io.nobt.core.commands.RetrieveNobtCommand;
import io.nobt.core.domain.*;
import io.nobt.persistence.NobtRepositoryCommandInvoker;
import io.nobt.rest.payloads.CreateNobtInput;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.zalando.problem.Problem;
import org.zalando.problem.ThrowableProblem;
import spark.ExceptionHandler;
import spark.Request;
import spark.Response;
import spark.Service;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.Closeable;
import java.io.IOException;
import java.util.Set;

import static javax.ws.rs.core.Response.Status.*;

public class NobtRestApi implements Closeable {

    private static final Logger LOGGER = LogManager.getLogger(NobtRestApi.class);

    private final Service http;
    private final NobtRepositoryCommandInvoker nobtRepositoryCommandInvoker;
    private final BodyParser bodyParser;
    private final ObjectMapper objectMapper;
    private final NobtFactory nobtFactory;

    public NobtRestApi(Service service, NobtRepositoryCommandInvoker nobtRepositoryCommandInvoker, BodyParser bodyParser, ObjectMapper objectMapper, NobtFactory nobtFactory) {
        this.http = service;
        this.nobtRepositoryCommandInvoker = nobtRepositoryCommandInvoker;
        this.bodyParser = bodyParser;
        this.objectMapper = objectMapper;
        this.nobtFactory = nobtFactory;
    }

    public void run(int port) {
        http.port(port);

        setupCORS();

        registerApplicationRoutes();
        registerTestFailRoute();

        registerUnknownNobtExceptionHandler();
        registerConversionInformationInconsistentExceptionHandler();
        registerValidationErrorExceptionHandler();
        registerUncaughtExceptionHandler();
    }

    private void registerApplicationRoutes() {
        registerCreateNobtRoute();
        registerRetrieveNobtRoute();
        registerCreateExpenseRoute();
        registerCreatePaymentRoute();
        registerDeleteExpenseRoute();
    }

    private void registerCreateExpenseRoute() {
        http.post("/nobts/:nobtId/expenses", "application/json", (req, resp) -> {

            final NobtId nobtId = decodeNobtIdentifierToDatabaseId(req);
            final ExpenseDraft expenseDraft = bodyParser.parseBodyAs(req, ExpenseDraft.class);


            nobtRepositoryCommandInvoker.invoke(new CreateExpenseCommand(nobtId, expenseDraft));

            // TODO return id of expense
            resp.status(201);

            return "";
        });
    }

    private void registerCreatePaymentRoute() {
        http.post("/nobts/:nobtId/payments", "application/json", (req, resp) -> {

            final NobtId nobtId = decodeNobtIdentifierToDatabaseId(req);
            final PaymentDraft paymentDraft = bodyParser.parseBodyAs(req, PaymentDraft.class);


            nobtRepositoryCommandInvoker.invoke(new CreatePaymentCommand(nobtId, paymentDraft));

            // TODO return id of payment
            resp.status(201);

            return "";
        });
    }

    private void registerDeleteExpenseRoute() {

        http.delete("/nobts/:nobtId/expenses/:expenseId", (req, res) -> {

            final NobtId nobtId = decodeNobtIdentifierToDatabaseId(req);
            final Long expenseId = Long.parseLong(req.params(":expenseId"));


            nobtRepositoryCommandInvoker.invoke(new DeleteExpenseCommand(nobtId, expenseId));


            res.status(204);

            return "";
        });
    }

    private void registerRetrieveNobtRoute() {
        http.get("/nobts/:nobtId", (req, res) -> {

            final NobtId nobtId = decodeNobtIdentifierToDatabaseId(req);


            final Nobt nobt = nobtRepositoryCommandInvoker.invoke(new RetrieveNobtCommand(nobtId));


            res.header("Content-Type", "application/json");

            return nobt;
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

            return nobt;
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

    private void registerUnknownNobtExceptionHandler() {
        http.exception(UnknownNobtException.class, (e, request, response) -> {

            final ThrowableProblem problem = Problem.builder()
                    .withStatus(NOT_FOUND)
                    .withDetail("The nobt you are looking for cannot be found.")
                    .build();

            writeProblemAsJsonToResponse(response, problem);
        });
    }

    private void registerConversionInformationInconsistentExceptionHandler() {
        http.exception(ConversionInformationInconsistentException.class, (e, request, response) -> {

            final ConversionInformationInconsistentException exception = (ConversionInformationInconsistentException) e;

            final ThrowableProblem problem = Problem.builder()
                    .withStatus(BAD_REQUEST)
                    .withTitle("The supplied conversion information is inconsistent.")
                    .withDetail("Either supply a foreign currency different from the nobt-currency or a rate of 1.")
                    .with("nobtCurrency", exception.getNobtCurrencyKey())
                    .with("foreignCurrency", exception.getConversionInformation().getForeignCurrencyKey())
                    .with("foreignCurrencyRate", exception.getConversionInformation().getRate())
                    .build();

            writeProblemAsJsonToResponse(response, problem);
        });
    }

    private void registerValidationErrorExceptionHandler() {
        http.exception(ConstraintViolationException.class, (ve, request, response) -> {

            final Set<ConstraintViolation<?>> violations = ((ConstraintViolationException) ve).getConstraintViolations();

            writeProblemAsJsonToResponse(response, new ValidationProblem(violations));
        });
    }

    private void registerUncaughtExceptionHandler() {
        http.exception(Exception.class, new UncaughtExceptionHandler());
        http.exception(RuntimeException.class, new UncaughtExceptionHandler());
    }

    @Override
    public void close() throws IOException {
        http.stop();
        nobtRepositoryCommandInvoker.close();
    }

    private class UncaughtExceptionHandler implements ExceptionHandler {

        @Override
        public void handle(Exception e, Request request, Response response) {

            LOGGER.error(NobtApplication.SENTRY, "Unhandled exception.", e);

            final ThrowableProblem internalProblem = Problem
                    .builder()
                    .withTitle("Internal error")
                    .withStatus(INTERNAL_SERVER_ERROR)
                    .withDetail("An unexpected internal error occurred. The problem has been reported.")
                    .build();

            writeProblemAsJsonToResponse(response, internalProblem);
        }
    }

    private void writeProblemAsJsonToResponse(Response response, Problem problem) {
        try {
            response.status(problem.getStatus().getStatusCode());
            response.body(objectMapper.writeValueAsString(problem));
            response.header("Content-Type", "application/problem+json");
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }
}
