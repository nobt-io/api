package io.nobt.rest;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import io.nobt.config.Config;
import io.nobt.core.NobtCalculator;
import io.nobt.core.UnknownNobtException;
import io.nobt.persistence.NobtDao;
import io.nobt.persistence.dao.NobtDaoImpl;
import io.nobt.persistence.dao.NobtMapper;
import io.nobt.rest.encoding.EncodingNotSpecifiedException;
import io.nobt.rest.filter.EncodingAwareBodyParser;
import io.nobt.rest.handler.CreateExpenseHandler;
import io.nobt.rest.handler.CreateNobtHandler;
import io.nobt.rest.handler.GetNobtHandler;
import io.nobt.rest.handler.GetPersonsHandler;
import io.nobt.rest.json.GsonFactory;
import io.nobt.rest.json.JsonElementBodyParser;

import javax.persistence.EntityManager;

import static spark.Spark.*;

public class NobtApplication {

    public static void main(String[] args) {

        final Config config = Config.getConfigForCurrentEnvironment();

        port(config.getDatabasePort());

        final Gson gson = GsonFactory.createConfiguredGsonInstance();
        final EntityManagerFactoryProvider emfProvider = new EntityManagerFactoryProvider();

        final EntityManager entityManager = emfProvider.create(config.getDatabaseConfig()).createEntityManager();
        NobtDao nobtDao = new NobtDaoImpl(entityManager, new NobtMapper());
        NobtCalculator calculator = new NobtCalculator();

        JsonParser parser = new JsonParser();
        final JsonElementBodyParser bodyParser = new JsonElementBodyParser(parser);

        // Spark does not respect the encoding specified in the content-type header
        before(new EncodingAwareBodyParser());

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

        post("/nobts", "application/json", new CreateNobtHandler(nobtDao, gson, bodyParser));
        get("/nobts/:nobtId", new GetNobtHandler(nobtDao, gson, calculator));
        get("/nobts/:nobtId/persons", new GetPersonsHandler(nobtDao, gson));
        post("/nobts/:nobtId/expenses", "application/json", new CreateExpenseHandler(nobtDao, gson, bodyParser));

        exception(EncodingNotSpecifiedException.class, (exception, request, response) -> {
            response.status(400);
            response.body("Please specify a charset for your content!");
        });

        exception(UnknownNobtException.class, ((e, request, response) -> {
            response.status(404);
            response.body(e.getMessage());
        }));
    }
}
