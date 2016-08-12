package io.nobt.persistence.mapping.bson;

import com.fasterxml.jackson.databind.SerializerProvider;
import de.undercouch.bson4jackson.BsonGenerator;
import de.undercouch.bson4jackson.serializers.BsonSerializer;
import io.nobt.core.domain.Person;

import java.io.IOException;

public class BsonPersonSerializer extends BsonSerializer<Person> {
    @Override
    public void serialize(Person person, BsonGenerator bsonGenerator, SerializerProvider serializerProvider) throws IOException {
        bsonGenerator.writeString(person.getName());
    }
}
