package io.nobt.rest.handler;

import com.google.gson.Gson;
import io.nobt.core.domain.Nobt;
import io.nobt.core.domain.Person;
import io.nobt.persistence.NobtDao;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.Set;
import java.util.UUID;

public class GetPersonsHandler implements Route {

	private final NobtDao nobtDao;
	private final Gson gson;

	public GetPersonsHandler(NobtDao nobtDao, Gson gson) {
		this.nobtDao = nobtDao;
		this.gson = gson;
	}

	@Override
	public Object handle(Request request, Response response) throws Exception {

		final UUID nobtId = UUID.fromString(request.params(":nobtId"));
		final Nobt nobt = nobtDao.find(nobtId);
		final Set<Person> participatingPersons = nobt.getParticipatingPersons();

		response.header("Content-Type", "application/json");

		return gson.toJson(participatingPersons);
	}
}
