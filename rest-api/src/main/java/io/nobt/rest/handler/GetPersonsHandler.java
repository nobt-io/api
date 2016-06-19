package io.nobt.rest.handler;

import io.nobt.core.domain.Nobt;
import io.nobt.persistence.NobtDao;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.UUID;

public class GetPersonsHandler implements Route {

	private final NobtDao nobtDao;

	public GetPersonsHandler(NobtDao nobtDao) {
		this.nobtDao = nobtDao;
	}

	@Override
	public Object handle(Request request, Response response) throws Exception {

		final UUID nobtId = UUID.fromString(request.params(":nobtId"));
		final Nobt nobt = nobtDao.get(nobtId);

		return nobt.getParticipatingPersons();
	}
}
