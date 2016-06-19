package io.nobt.rest.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.nobt.core.domain.Amount;

import java.io.IOException;

public class AmountSerializer extends JsonSerializer<Amount> {
    @Override
    public void serialize(Amount value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeNumber(value.getRoundedValue());
    }
}
