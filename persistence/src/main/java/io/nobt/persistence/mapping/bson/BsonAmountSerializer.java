package io.nobt.persistence.mapping.bson;

import com.fasterxml.jackson.databind.SerializerProvider;
import de.undercouch.bson4jackson.BsonGenerator;
import de.undercouch.bson4jackson.serializers.BsonSerializer;
import io.nobt.core.domain.Amount;

import java.io.IOException;

public class BsonAmountSerializer extends BsonSerializer<Amount> {
    @Override
    public void serialize(Amount amount, BsonGenerator bsonGenerator, SerializerProvider serializerProvider) throws IOException {
        bsonGenerator.writeNumber(amount.getRoundedValue());
    }
}
