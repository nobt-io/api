package io.nobt.rest;

import io.nobt.core.NobtCalculator;
import io.nobt.core.UnknownNobtException;
import io.nobt.persistence.NobtDao;
import io.nobt.rest.encoding.EncodingNotSpecifiedException;
import io.nobt.rest.filter.EncodingAwareBodyParser;
import io.nobt.rest.handler.CreateExpenseHandler;
import io.nobt.rest.handler.CreateNobtHandler;
import io.nobt.rest.handler.GetNobtHandler;
import io.nobt.rest.handler.GetPersonsHandler;
import io.nobt.rest.json.BodyParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import spark.ExceptionHandler;
import spark.ResponseTransformer;

import javax.validation.ConstraintViolationException;

import static spark.Spark.*;

public class NobtRestApi {

    private static final Logger UNHANDLED_EXCEPTION_LOGGER = LogManager.getLogger("io.nobt.rest.NobtApplication.unhandledExceptions");

    private final NobtDao nobtDao;
    private final NobtCalculator nobtCalculator;
    private final BodyParser bodyParser;
    private final ResponseTransformer jsonResponseTransformer;
    private final ExceptionHandler cvExceptionHandler;

    public NobtRestApi(NobtDao nobtDao, NobtCalculator nobtCalculator, BodyParser bodyParser, ResponseTransformer jsonResponseTransformer, ExceptionHandler cvExceptionHandler) {
        this.nobtDao = nobtDao;
        this.nobtCalculator = nobtCalculator;
        this.bodyParser = bodyParser;
        this.jsonResponseTransformer = jsonResponseTransformer;
        this.cvExceptionHandler = cvExceptionHandler;
    }

    public void run(int port) {
        port(port);

        parseBodyWithEncodingSpecifiedInContentType();
        useApplicationJsonAsDefaultReponseContentType();
        setupCORS();

        post("/nobts", "application/json", new CreateNobtHandler(nobtDao, bodyParser), jsonResponseTransformer);

        // deprecated
        get("/nobts/:nobtId/persons", new GetPersonsHandler());

        get("/nobts/:nobtId", new GetNobtHandler(nobtDao, nobtCalculator), jsonResponseTransformer);
        post("/nobts/:nobtId/expenses", "application/json", new CreateExpenseHandler(nobtDao, bodyParser), jsonResponseTransformer);

        exception(EncodingNotSpecifiedException.class, (exception, request, response) -> {
            response.status(400);
            response.body("Please specify a charset for your content!");
        });

        exception(UnknownNobtException.class, ((e, request, response) -> {
            response.status(404);
            response.body(e.getMessage());
        }));

        exception(ConstraintViolationException.class, cvExceptionHandler);

        handleUnknownExceptions();
    }

    private void parseBodyWithEncodingSpecifiedInContentType() {
        before(new EncodingAwareBodyParser());
    }

    private void useApplicationJsonAsDefaultReponseContentType() {
        before((request, response) -> response.header("Content-Type", "application/json"));
    }

    private void setupCORS() {
        before((req, res) -> {
            res.header("Access-Control-Allow-Origin", "*");
            res.header("Access-Control-Request-Method", "*");
            res.header("Access-Control-Allow-Headers", "*");
        });

        options("/*", (req, res) -> {
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

    private void handleUnknownExceptions() {
        exception(Exception.class, (e, request, response) -> {
            UNHANDLED_EXCEPTION_LOGGER.error("Unhandled exception", e);
            response.status(500);
        });

        exception(RuntimeException.class, (e, request, response) -> {
            UNHANDLED_EXCEPTION_LOGGER.error("Unhandled exception", e);
            response.status(500);
        });
    }
}
