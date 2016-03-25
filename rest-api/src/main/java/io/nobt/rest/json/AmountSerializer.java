package io.nobt.rest.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import io.nobt.core.domain.Amount;

import java.lang.reflect.Type;

/**
 * @author Thomas Eizinger, Senacor Technologies AG.
 */
public class AmountSerializer implements JsonSerializer<Amount> {
	@Override
	public JsonElement serialize(Amount src, Type typeOfSrc, JsonSerializationContext context) {
		return new JsonPrimitive(src.getRoundedValue());
	}
}
