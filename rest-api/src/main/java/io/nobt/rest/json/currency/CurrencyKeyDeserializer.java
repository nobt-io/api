package io.nobt.rest.json.currency;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import io.nobt.core.domain.CurrencyKey;

import java.io.IOException;

public class CurrencyKeyDeserializer extends JsonDeserializer<CurrencyKey> {

    @Override
    public CurrencyKey deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return new CurrencyKey(p.getText());
    }
}
