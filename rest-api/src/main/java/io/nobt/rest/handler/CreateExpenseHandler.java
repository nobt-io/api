package io.nobt.rest.handler;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.nobt.core.domain.Expense;
import io.nobt.core.domain.Person;
import io.nobt.persistence.NobtDao;
import io.nobt.rest.json.BodyParser;
import spark.Request;
import spark.Response;
import spark.Route;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

public class CreateExpenseHandler implements Route {

	private final NobtDao nobtDao;
	private final BodyParser bodyParser;

	public CreateExpenseHandler(NobtDao nobtDao, BodyParser bodyParser) {
		this.nobtDao = nobtDao;
		this.bodyParser = bodyParser;
	}

	@Override
	public Object handle(Request req, Response resp) throws Exception {

		UUID nobtId = UUID.fromString(req.params(":nobtId"));
		final Input input = bodyParser.parseBodyAs(req, Input.class);

		Expense expense = nobtDao.createExpense(nobtId, input.name, input.amount, input.debtee, input.debtors);
		resp.status(201);

		return expense;
	}

	public static class Input {

		@JsonProperty("name")
		private String name;

		@JsonProperty("amount")
		private BigDecimal amount;

		@Valid
		@JsonProperty("debtee")
		private Person debtee;

		@Valid
		@JsonProperty("debtors")
		private Set<Person> debtors;
	}
}
