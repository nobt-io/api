package io.nobt.rest.handler;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.nobt.core.domain.Nobt;
import io.nobt.core.domain.Person;
import io.nobt.persistence.NobtDao;
import io.nobt.rest.json.BodyParser;
import org.hibernate.validator.constraints.NotEmpty;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.Collections;
import java.util.Set;

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

		Nobt nobt = nobtDao.create(input.nobtName, input.explicitParticipants);

		resp.header("Location", req.url() + "/" + nobt.getId());
		resp.status(201);

		// TODO do not return this but instead let client rely on link
		return new GetNobtHandler.Output(nobt, Collections.emptySet());
	}

	public static class Input {

		@NotEmpty
		@JsonProperty("nobtName")
		private String nobtName;

		@NotEmpty
		@JsonProperty("explicitParticipants")
		private Set<Person> explicitParticipants;
	}
}
