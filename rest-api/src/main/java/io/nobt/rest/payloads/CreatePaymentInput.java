package io.nobt.rest.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.nobt.core.domain.Amount;
import io.nobt.core.domain.Person;
import io.nobt.core.validation.Positive;
import org.hibernate.validator.constraints.NotEmpty;

public class CreatePaymentInput {

    @NotEmpty
    @JsonProperty(value = "sender", required = true)
    public Person sender;

    @NotEmpty
    @JsonProperty(value = "recipient", required = true)
    public Person recipient;

    @Positive
    @JsonProperty(value = "amount", required = true)
    public Amount amount;

    @NotEmpty
    @JsonProperty(value = "description")
    public String description;
}
