package io.nobt.rest.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import spark.Request;

public class JsonElementBodyParser {

	private final JsonParser jsonParser;

	public JsonElementBodyParser(JsonParser jsonParser) {
		this.jsonParser = jsonParser;
	}

	public JsonElement parse(Request request) {

		final Object body = request.attribute("body");

		if (body == null || !(body instanceof String)) {
			throw new IllegalStateException();
		}

		return jsonParser.parse((String) body);
	}
}
