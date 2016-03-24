package io.nobt.rest;

import static spark.Spark.get;
import static spark.Spark.post;

import com.google.gson.Gson;
import com.google.gson.JsonParser;

import io.nobt.core.NobtCalculator;
import io.nobt.persistence.NobtDao;
import io.nobt.persistence.dao.InMemoryNobtDao;
import io.nobt.rest.handler.CreateExpenseHandler;
import io.nobt.rest.handler.CreateNobtHandler;
import io.nobt.rest.handler.GetNobtHandler;

public class NobtApplication {

	public static void main(String[] args) {

		Gson gson = new Gson();
		JsonParser parser = new JsonParser();
		NobtDao nobtDao = new InMemoryNobtDao();
		NobtCalculator calculator = new NobtCalculator();

		// port(Integer.parseInt(System.getenv("PORT")));

		post("/nobts", new CreateNobtHandler(nobtDao, gson, parser));
		get("/nobts/:nobtId", new GetNobtHandler(nobtDao, gson, calculator));
		post("/nobts/:nobtId/expenses", new CreateExpenseHandler(nobtDao, gson, parser));
	}
}
