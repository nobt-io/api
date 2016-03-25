package io.nobt.rest.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.nobt.core.domain.Amount;
import io.nobt.core.domain.Person;

public class GsonFactory {

	public static Gson createConfiguredGsonInstance() {
		return new GsonBuilder()
				.registerTypeAdapter(Person.class, new PersonSerializer())
				.registerTypeAdapter(Amount.class, new AmountSerializer())
				.setPrettyPrinting()
				.create();
	}

}
