/**
 * 
 */
package io.nobt.rest.handler;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.nobt.core.domain.Nobt;
import io.nobt.persistence.NobtDao;
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
	private JsonParser parser;

	public CreateNobtHandler(NobtDao nobtDao, Gson gson, JsonParser parser) {
		this.nobtDao = nobtDao;
		this.gson = gson;
		this.parser = parser;
	}

	@Override
	public Object handle(Request req, Response resp) throws Exception {

		final String encoding = req.attribute("Content-Charset");
		final String body = new String(req.bodyAsBytes(), encoding);

		JsonObject o = parser.parse(body).getAsJsonObject();

		Nobt nobt = nobtDao.create(o.get("nobtName").getAsString());

		resp.header("Location", req.url() + "/" + nobt.getId());
		resp.status(201);

		return gson.toJson(nobt);
	}

}
