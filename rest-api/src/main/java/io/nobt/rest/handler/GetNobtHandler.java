package io.nobt.rest.handler;

import java.util.Set;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import io.nobt.core.NobtCalculator;
import io.nobt.core.Transaction;
import io.nobt.core.domain.Nobt;
import io.nobt.persistence.NobtDao;
import spark.Request;
import spark.Response;
import spark.Route;

public class GetNobtHandler implements Route {

	private NobtDao nobtDao;
	private Gson gson;
	private NobtCalculator calculator;

	public GetNobtHandler(NobtDao nobtDao, Gson gson, NobtCalculator calculator) {
		this.nobtDao = nobtDao;
		this.gson = gson;
		this.calculator = calculator;
	}

	@Override
	public Object handle(Request req, Response resp) throws Exception {
		Nobt nobt = nobtDao.find(UUID.fromString(req.params(":nobtId")));

		Set<Transaction> transactions = calculator.calculate(nobt);

		JsonObject json = gson.toJsonTree(nobt).getAsJsonObject();
		json.add("transactions", gson.toJsonTree(transactions));

		return gson.toJson(json);
	}

}
