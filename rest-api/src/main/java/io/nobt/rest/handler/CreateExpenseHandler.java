package io.nobt.rest.handler;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.nobt.core.domain.Expense;
import io.nobt.core.domain.Person;
import io.nobt.persistence.NobtDao;
import io.nobt.rest.json.JsonElementBodyParser;
import spark.Request;
import spark.Response;
import spark.Route;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class CreateExpenseHandler implements Route {

	private NobtDao nobtDao;
	private Gson gson;
	private JsonElementBodyParser parser;

	public CreateExpenseHandler(NobtDao nobtDao, Gson gson, JsonElementBodyParser parser) {
		this.nobtDao = nobtDao;
		this.gson = gson;
		this.parser = parser;
	}

	@Override
	public Object handle(Request req, Response resp) throws Exception {

		JsonObject o = parser.parse(req).getAsJsonObject();

		UUID nobtId = UUID.fromString(req.params(":nobtId"));
		String name = o.get("name").getAsString();
		BigDecimal amount = o.get("amount").getAsBigDecimal();
		Person debtee = Person.forName(o.get("debtee").getAsString());
		Set<Person> debtors = new HashSet<>();
		o.get("debtors").getAsJsonArray().forEach((debtor) -> debtors.add(Person.forName(debtor.getAsString())));

		Expense expense = nobtDao.createExpense(nobtId, name, amount, debtee, debtors);
		resp.status(201);
		return gson.toJson(expense);
	}
}
