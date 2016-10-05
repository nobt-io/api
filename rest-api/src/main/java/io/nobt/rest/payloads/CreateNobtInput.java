package io.nobt.rest.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.nobt.core.domain.CurrencyKey;
import io.nobt.core.domain.Person;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Set;

public class CreateNobtInput {

    @NotEmpty
    @JsonProperty("nobtName")
    public String nobtName;

    @NotNull
    @Valid
    @JsonProperty("currency")
    public CurrencyKey currencyKey;

    @NotEmpty
    @JsonProperty("explicitParticipants")
    public Set<Person> explicitParticipants;
}
