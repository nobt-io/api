package io.nobt.rest.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.nobt.core.domain.Person;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.Set;

public class CreateNobtInput {

    @NotEmpty
    @JsonProperty("nobtName")
    public String nobtName;

    @NotEmpty
    @JsonProperty("explicitParticipants")
    public Set<Person> explicitParticipants;
}
