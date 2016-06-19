package io.nobt.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.nobt.config.Config;
import io.nobt.core.NobtCalculator;
import io.nobt.core.UnknownNobtException;
import io.nobt.persistence.NobtDao;
import io.nobt.persistence.dao.NobtDaoImpl;
import io.nobt.persistence.dao.NobtMapper;
import io.nobt.rest.encoding.EncodingNotSpecifiedException;
import io.nobt.rest.filter.EncodingAwareBodyParser;
import io.nobt.rest.handler.*;
import io.nobt.rest.json.BodyParser;
import io.nobt.rest.json.JacksonResponseTransformer;
import io.nobt.rest.json.ObjectMapperFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import spark.ResponseTransformer;

import javax.persistence.EntityManager;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;

import static spark.Spark.*;

public class NobtApplication {

    private static final Logger UNHANDLED_EXCEPTION_LOGGER = LogManager.getLogger("io.nobt.rest.NobtApplication.unhandledExceptions");

    private static final ObjectMapperFactory objectMapperFactory = new ObjectMapperFactory();
    private static final EntityManagerFactoryProvider emfProvider = new EntityManagerFactoryProvider();

    public static void main(String[] args) {

        final Config config = Config.getConfigForCurrentEnvironment();
        final ObjectMapper objectMapper = objectMapperFactory.create();

        final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        final BodyParser bodyParser = new BodyParser(objectMapper, validator);

        final ResponseTransformer jsonResponseTransformer = new JacksonResponseTransformer(objectMapper);

        final EntityManager entityManager = emfProvider.create(config.getDatabaseConfig()).createEntityManager();
        NobtDao nobtDao = new NobtDaoImpl(entityManager, new NobtMapper());
        NobtCalculator calculator = new NobtCalculator();

        port(config.getDatabasePort());

        parseBodyWithEncodingSpecifiedInContentType();
        useApplicationJsonAsDefaultReponseContentType();
        setupCORS();

        post("/nobts", "application/json", new CreateNobtHandler(nobtDao, bodyParser), jsonResponseTransformer);

        // deprecated
        get("/nobts/:nobtId/persons", new GetPersonsHandler());

        get("/nobts/:nobtId", new GetNobtHandler(nobtDao, calculator), jsonResponseTransformer);
        post("/nobts/:nobtId/expenses", "application/json", new CreateExpenseHandler(nobtDao, bodyParser), jsonResponseTransformer);


        exception(EncodingNotSpecifiedException.class, (exception, request, response) -> {
            response.status(400);
            response.body("Please specify a charset for your content!");
        });

        exception(UnknownNobtException.class, ((e, request, response) -> {
            response.status(404);
            response.body(e.getMessage());
        }));

        exception(ConstraintViolationException.class, new ConstraintViolationExceptionHandler(objectMapper));

        handleUnknownExceptions();
    }

    private static void parseBodyWithEncodingSpecifiedInContentType() {
        before(new EncodingAwareBodyParser());
    }

    private static void useApplicationJsonAsDefaultReponseContentType() {
        before((request, response) -> response.header("Content-Type", "application/json"));
    }

    private static void setupCORS() {
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

    private static void handleUnknownExceptions() {
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
