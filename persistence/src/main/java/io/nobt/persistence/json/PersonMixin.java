package io.nobt.persistence.json;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.nobt.core.domain.Person;

public abstract class PersonMixin extends Person {

    @JsonCreator
    public PersonMixin(@JsonProperty("name") String name) {
        super(name);
    }
}
