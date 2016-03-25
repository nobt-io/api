package io.nobt.rest;

import com.google.gson.Gson;
import com.google.gson.JsonParser;

import io.nobt.core.NobtCalculator;
import io.nobt.persistence.NobtDao;
import io.nobt.persistence.dao.InMemoryNobtDao;
import io.nobt.rest.config.Config;
import io.nobt.rest.handler.CreateExpenseHandler;
import io.nobt.rest.handler.CreateNobtHandler;
import io.nobt.rest.handler.GetNobtHandler;
import io.nobt.rest.handler.GetPersonsHandler;
import io.nobt.rest.json.GsonFactory;

import static spark.Spark.*;

public class NobtApplication {

	public static void main(String[] args) {

		final Config config = Config.getConfigForCurrentEnvironment();
		final Gson gson = GsonFactory.createConfiguredGsonInstance();

		port(config.getPort());

		JsonParser parser = new JsonParser();
		NobtDao nobtDao = new InMemoryNobtDao();
		NobtCalculator calculator = new NobtCalculator();

		post("/nobts", new CreateNobtHandler(nobtDao, gson, parser));
		get("/nobts/:nobtId", new GetNobtHandler(nobtDao, gson, calculator));
		get("/nobts/:nobtId/persons", new GetPersonsHandler(nobtDao, gson));
		post("/nobts/:nobtId/expenses", new CreateExpenseHandler(nobtDao, gson, parser));

		after((req, res) -> res.header("Content-Type", "application/json"));
	}
}
