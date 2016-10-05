package io.nobt.rest.json.currency;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.nobt.core.domain.CurrencyKey;

import java.io.IOException;

public class CurrencyKeySerializer extends JsonSerializer<CurrencyKey> {

    @Override
    public void serialize(CurrencyKey value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(value.getKey());
    }
}
