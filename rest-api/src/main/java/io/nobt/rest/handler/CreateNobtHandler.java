package io.nobt.rest.handler;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.nobt.core.domain.Nobt;
import io.nobt.persistence.NobtDao;
import io.nobt.rest.json.BodyParser;
import spark.Request;
import spark.Response;
import spark.Route;

public class CreateNobtHandler implements Route {

	private final NobtDao nobtDao;
	private final BodyParser bodyParser;

	public CreateNobtHandler(NobtDao nobtDao, BodyParser bodyParser) {
		this.nobtDao = nobtDao;
		this.bodyParser = bodyParser;
	}

	@Override
	public Object handle(Request req, Response resp) throws Exception {

		final Input input = bodyParser.parseBodyAs(req, Input.class);

		Nobt nobt = nobtDao.create(input.nobtName);

		resp.header("Location", req.url() + "/" + nobt.getId());
		resp.status(201);

		return nobt;
	}

	public static class Input {

		@JsonProperty("nobtName")
		private String nobtName;
	}
}
