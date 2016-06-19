package io.nobt.rest.json.expense;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.nobt.core.domain.Amount;

@JsonPropertyOrder({ "name", "debtee", "amount", "debtors" })
public abstract class ExpenseMixin {

    @JsonProperty("amount")
    public abstract Amount getOverallAmount();

    @JsonIgnore
    public abstract Amount getAmountPerDebtor();
}
