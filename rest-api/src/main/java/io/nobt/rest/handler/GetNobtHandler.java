package io.nobt.rest.handler;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import io.nobt.core.NobtCalculator;
import io.nobt.core.domain.Nobt;
import io.nobt.core.domain.Transaction;
import io.nobt.persistence.NobtDao;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.Set;
import java.util.UUID;

public class GetNobtHandler implements Route {

	private NobtDao nobtDao;
	private NobtCalculator calculator;

	public GetNobtHandler(NobtDao nobtDao, NobtCalculator calculator) {
		this.nobtDao = nobtDao;
		this.calculator = calculator;
	}

	@Override
	public Object handle(Request req, Response res) throws Exception {

		final UUID nobtId = UUID.fromString(req.params(":nobtId"));

		Nobt nobt = nobtDao.get(nobtId);
		Set<Transaction> transactions = calculator.calculate(nobt);

		return new Output(nobt, transactions);
	}

	public static class Output {

		@JsonUnwrapped
		private Nobt nobt;
		private Set<Transaction> transactions;

		public Output(Nobt nobt, Set<Transaction> transactions) {
			this.nobt = nobt;
			this.transactions = transactions;
		}
	}
}
