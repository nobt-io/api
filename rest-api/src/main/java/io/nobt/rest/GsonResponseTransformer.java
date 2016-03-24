package io.nobt.rest;

import com.google.gson.Gson;

import spark.ResponseTransformer;

public class GsonResponseTransformer implements ResponseTransformer {

	private Gson gson;

	public GsonResponseTransformer(Gson gson) {
		this.gson = gson;
	}

	@Override
	public String render(Object in) throws Exception {
		return gson.toJson(in);
	}

}
