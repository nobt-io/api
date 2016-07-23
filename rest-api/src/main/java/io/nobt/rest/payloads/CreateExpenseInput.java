package io.nobt.rest.payloads;

import java.math.BigDecimal;
import java.util.Set;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.nobt.core.domain.Person;

public class CreateExpenseInput {

    @JsonProperty("name")
    public String name;

    @JsonProperty("amount")
    public BigDecimal amount;

    @Valid
    @JsonProperty("debtee")
    public Person debtee;

    @Valid
    @JsonProperty("debtors")
    public Set<Person> debtors;
}
