package io.nobt.rest.payloads;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.hibernate.validator.constraints.NotEmpty;

import io.nobt.core.domain.Person;

public class CreateNobtInput {

    @NotEmpty
    @JsonProperty("nobtName")
    public String nobtName;

    @NotEmpty
    @JsonProperty("explicitParticipants")
    public Set<Person> explicitParticipants;
}
