package io.nobt.rest;

import io.nobt.core.NobtCalculator;
import io.nobt.core.UnknownNobtException;
import io.nobt.persistence.NobtDao;
import io.nobt.profiles.Profiles;
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
import spark.Response;
import spark.ResponseTransformer;
import spark.Service;

import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.io.PrintStream;

import static io.nobt.profiles.Profiles.ifProfile;

public class NobtRestApi {

    private static final Logger LOGGER = LogManager.getLogger(NobtRestApi.class);
    private static final Logger UNHANDLED_EXCEPTION_LOGGER = LogManager.getLogger("io.nobt.rest.NobtApplication.unhandledExceptions");

    private final Service http;
    private final NobtDao nobtDao;
    private final NobtCalculator nobtCalculator;
    private final BodyParser bodyParser;
    private final ResponseTransformer jsonResponseTransformer;
    private final ExceptionHandler cvExceptionHandler;

    public NobtRestApi(Service http, NobtDao nobtDao, NobtCalculator nobtCalculator, BodyParser bodyParser, ResponseTransformer jsonResponseTransformer, ExceptionHandler cvExceptionHandler) {
        this.http = http;
        this.nobtDao = nobtDao;
        this.nobtCalculator = nobtCalculator;
        this.bodyParser = bodyParser;
        this.jsonResponseTransformer = jsonResponseTransformer;
        this.cvExceptionHandler = cvExceptionHandler;
    }

    public void run(int port) {
        http.port(port);

        parseBodyWithEncodingSpecifiedInContentType();
        useApplicationJsonAsDefaultReponseContentType();
        setupCORS();

        http.post("/nobts", "application/json", new CreateNobtHandler(nobtDao, bodyParser), jsonResponseTransformer);

        // deprecated
        http.get("/nobts/:nobtId/persons", new GetPersonsHandler());

        http.get("/nobts/:nobtId", new GetNobtHandler(nobtDao, nobtCalculator), jsonResponseTransformer);
        http.post("/nobts/:nobtId/expenses", "application/json", new CreateExpenseHandler(nobtDao, bodyParser), jsonResponseTransformer);

        http.exception(EncodingNotSpecifiedException.class, (exception, request, response) -> {
            response.status(400);
            response.body("Please specify a charset for your content!");
        });

        http.exception(UnknownNobtException.class, ((e, request, response) -> {
            response.status(404);
            response.body(e.getMessage());
        }));

        http.exception(ConstraintViolationException.class, cvExceptionHandler);

        handleUnknownExceptions();
    }

    private void parseBodyWithEncodingSpecifiedInContentType() {
        http.before(new EncodingAwareBodyParser());
    }

    private void useApplicationJsonAsDefaultReponseContentType() {
        http.before((request, response) -> response.header("Content-Type", "application/json"));
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

    private void handleUnknownExceptions() {
        http.exception(Exception.class, (e, request, response) -> {
            UNHANDLED_EXCEPTION_LOGGER.error("Unhandled exception", e);
            response.status(500);

            ifProfile( Profiles::notCloud, () -> printStacktraceToResponse(e, response) );
        });

        http.exception(RuntimeException.class, (e, request, response) -> {
            UNHANDLED_EXCEPTION_LOGGER.error("Unhandled exception", e);
            response.status(500);

            ifProfile( Profiles::notCloud, () -> printStacktraceToResponse(e, response) );
        });
    }

    private void printStacktraceToResponse(Exception e, Response response) {
        try {
            e.printStackTrace(new PrintStream(response.raw().getOutputStream()));
        } catch (IOException e1) {
            LOGGER.error("Failed to write stacktrace to response", e1);
        }
    }
}
