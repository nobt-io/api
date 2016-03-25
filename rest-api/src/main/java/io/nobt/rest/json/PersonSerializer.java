package io.nobt.rest.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import io.nobt.core.domain.Person;

import java.lang.reflect.Type;

public class PersonSerializer implements JsonSerializer<Person> {

	@Override
	public JsonElement serialize(Person p, Type typeOfSrc, JsonSerializationContext context) {
		return new JsonPrimitive(p.getName());
	}
}
