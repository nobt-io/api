package io.nobt.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.nobt.core.NobtCalculator;
import io.nobt.core.UnknownNobtException;
import io.nobt.core.domain.Nobt;
import io.nobt.core.domain.NobtFactory;
import io.nobt.core.domain.NobtId;
import io.nobt.core.domain.Transaction;
import io.nobt.persistence.NobtRepository;
import io.nobt.profiles.Profiles;
import io.nobt.rest.json.BodyParser;
import io.nobt.rest.payloads.CreateExpenseInput;
import io.nobt.rest.payloads.CreateNobtInput;
import io.nobt.rest.payloads.NobtResource;
import io.nobt.rest.payloads.SimpleViolation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import spark.Request;
import spark.Response;
import spark.Service;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static io.nobt.profiles.Profiles.ifProfile;
import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toList;

public class NobtRestApi {

    private static final Logger LOGGER = LogManager.getLogger(NobtRestApi.class);
    private static final Logger UNHANDLED_EXCEPTION_LOGGER = LogManager.getLogger("io.nobt.rest.NobtApplication.unhandledExceptions");

    private final Service http;
    private final NobtRepository nobtRepository;
    private final NobtCalculator nobtCalculator;
    private final BodyParser bodyParser;
    private final ObjectMapper objectMapper;
    private final SimpleViolationFactory simpleViolationFactory;
    private final NobtFactory nobtFactory;

    public NobtRestApi(Service service, NobtRepository nobtRepository, NobtCalculator nobtCalculator, BodyParser bodyParser, ObjectMapper objectMapper, NobtFactory nobtFactory) {
        this.http = service;
        this.nobtRepository = nobtRepository;
        this.nobtCalculator = nobtCalculator;
        this.bodyParser = bodyParser;
        this.objectMapper = objectMapper;
        this.simpleViolationFactory = new SimpleViolationFactory(objectMapper);
        this.nobtFactory = nobtFactory;
    }

    public void run(int port) {
        http.port(port);

        setupCORS();

        registerApplicationRoutes();

        http.exception(UnknownNobtException.class, ((e, request, response) -> {
            response.status(404);
            response.body(e.getMessage());
        }));

        handleValidationExceptions();
        handleUnknownExceptions();
    }

    private void registerApplicationRoutes() {
        registerCreateNobtRoute();
        registerRetrieveNobtRoute();
        registerCreateExpenseRoute();
    }

    private void registerCreateExpenseRoute() {
        http.post("/nobts/:nobtId/expenses", "application/json", (req, resp) -> {

            final NobtId databaseId = decodeNobtIdentifierToDatabaseId(req);
            final CreateExpenseInput input = bodyParser.parseBodyAs(req, CreateExpenseInput.class);


            final Nobt nobt = nobtRepository.getById(databaseId);
            nobt.addExpense(input.name, input.splitStrategy, input.debtee, new HashSet<>(input.shares), input.date);
            nobtRepository.save(nobt);


            resp.status(201);

            return "";
        });
    }

    private void registerRetrieveNobtRoute() {
        http.get("/nobts/:nobtId", (req, res) -> {

            final NobtId databaseId = decodeNobtIdentifierToDatabaseId(req);


            final Nobt nobt = nobtRepository.getById(databaseId);
            final Set<Transaction> transactions = nobtCalculator.calculate(nobt);


            res.header("Content-Type", "application/json");

            return new NobtResource(nobt, transactions);
        }, objectMapper::writeValueAsString);
    }

    private void registerCreateNobtRoute() {
        http.post("/nobts", "application/json", (req, res) -> {

            final CreateNobtInput input = bodyParser.parseBodyAs(req, CreateNobtInput.class);


            final Nobt unpersistedNobt = nobtFactory.create(input.nobtName, input.explicitParticipants);
            final NobtId id = nobtRepository.save(unpersistedNobt);


            res.status(201);
            res.header("Location", req.url() + "/" + id.toExternalIdentifier());
            res.header("Content-Type", "application/json");

            final Nobt nobt = nobtRepository.getById(id);

            return new NobtResource(nobt, emptySet());
        }, objectMapper::writeValueAsString);
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

    private void handleValidationExceptions() {
        http.exception(ConstraintViolationException.class, (e, request, response) -> {

            final Set<ConstraintViolation<?>> violations = ((ConstraintViolationException) e).getConstraintViolations();
            final List<SimpleViolation> simpleViolations = violations.stream().map(simpleViolationFactory::create).collect(toList());

            response.status(400);

            try {
                response.body(objectMapper.writeValueAsString(simpleViolations));
            } catch (JsonProcessingException e1) {
                throw new IllegalStateException(e1);
            }
        });
    }

    private void handleUnknownExceptions() {
        http.exception(Exception.class, (e, request, response) -> {
            UNHANDLED_EXCEPTION_LOGGER.error("Unhandled exception", e);
            response.status(500);

            ifProfile(Profiles::notCloud, () -> printStacktraceToResponse(e, response));
        });

        http.exception(RuntimeException.class, (e, request, response) -> {
            UNHANDLED_EXCEPTION_LOGGER.error("Unhandled exception", e);
            response.status(500);

            ifProfile(Profiles::notCloud, () -> printStacktraceToResponse(e, response));
        });
    }

    private void printStacktraceToResponse(Exception uncaughtException, Response response) {
        try {
            uncaughtException.printStackTrace(new PrintStream(response.raw().getOutputStream()));
        } catch (IOException e) {
            LOGGER.error("Failed to write stacktrace to response", e);
        }
    }
}
