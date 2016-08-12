package io.nobt.persistence.mapping.bson;

import com.fasterxml.jackson.databind.DeserializationContext;
import de.undercouch.bson4jackson.BsonParser;
import de.undercouch.bson4jackson.deserializers.BsonDeserializer;
import io.nobt.core.domain.Person;

import java.io.IOException;

public class BsonPersonDeserializer extends BsonDeserializer<Person> {
    @Override
    public Person deserialize(BsonParser bp, DeserializationContext ctxt) throws IOException {
        return Person.forName(bp.getText());
    }
}
