/**
 * 
 */
package io.nobt.rest.handler;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.nobt.core.domain.Nobt;
import io.nobt.persistence.NobtDao;
import io.nobt.rest.json.JsonElementBodyParser;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * @author Matthias
 *
 */
public class CreateNobtHandler implements Route {

	private NobtDao nobtDao;
	private Gson gson;
	private JsonElementBodyParser parser;

	public CreateNobtHandler(NobtDao nobtDao, Gson gson, JsonElementBodyParser parser) {
		this.nobtDao = nobtDao;
		this.gson = gson;
		this.parser = parser;
	}

	@Override
	public Object handle(Request req, Response resp) throws Exception {

		JsonObject o = parser.parse(req).getAsJsonObject();

		Nobt nobt = nobtDao.create(o.get("nobtName").getAsString());

		resp.header("Location", req.url() + "/" + nobt.getId());
		resp.status(201);

		return gson.toJson(nobt);
	}

}
