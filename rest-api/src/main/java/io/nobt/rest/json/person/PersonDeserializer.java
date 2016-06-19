package io.nobt.rest.json.person;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import io.nobt.core.domain.Person;

import java.io.IOException;

public class PersonDeserializer extends JsonDeserializer<Person> {
    @Override
    public Person deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        return Person.forName(p.getValueAsString());
    }
}
