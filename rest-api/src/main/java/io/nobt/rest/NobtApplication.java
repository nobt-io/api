package io.nobt.rest;

import static spark.Spark.port;
import static spark.Spark.post;

import com.google.gson.Gson;
import com.google.gson.JsonParser;

import io.nobt.persistence.NobtDao;
import io.nobt.persistence.dao.InMemoryNobtDao;
import io.nobt.rest.handler.CreateExpenseHandler;
import io.nobt.rest.handler.CreateNobtHandler;

public class NobtApplication {

	public static void main(String[] args) {

		Gson gson = new Gson();
		JsonParser parser = new JsonParser();
		NobtDao nobtDao = new InMemoryNobtDao();

		port(Integer.parseInt(System.getenv("PORT")));

		post("/nobts", new CreateNobtHandler(nobtDao, gson, parser));
		post("/nobts/:nobtId/expenses", new CreateExpenseHandler(nobtDao, gson, parser));
	}
}
