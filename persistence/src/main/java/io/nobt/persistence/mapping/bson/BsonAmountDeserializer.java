package io.nobt.persistence.mapping.bson;

import com.fasterxml.jackson.databind.DeserializationContext;
import de.undercouch.bson4jackson.BsonParser;
import de.undercouch.bson4jackson.deserializers.BsonDeserializer;
import io.nobt.core.domain.Amount;

import java.io.IOException;

public class BsonAmountDeserializer extends BsonDeserializer<Amount> {
    @Override
    public Amount deserialize(BsonParser bp, DeserializationContext ctxt) throws IOException {
        return Amount.fromBigDecimal(bp.getDecimalValue());
    }
}
