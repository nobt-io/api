package io.nobt.rest.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import io.nobt.core.domain.Amount;

import java.io.IOException;

public class AmountDeserializer extends JsonDeserializer<Amount> {

    @Override
    public Amount deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {

        final Number amount = p.getValueAsDouble();

        return Amount.fromDouble(amount.doubleValue());
    }
}
