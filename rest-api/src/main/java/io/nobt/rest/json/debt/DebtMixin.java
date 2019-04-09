package io.nobt.rest.json.debt;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.nobt.core.domain.Amount;
import io.nobt.core.domain.Person;
import io.nobt.core.domain.debt.Debt;

import java.math.BigDecimal;

public abstract class DebtMixin extends Debt {

    protected DebtMixin(Person debtor, Amount amount, Person debtee) {
        super(debtor, amount, debtee);
    }

    @JsonProperty("amount")
    @Override
    public abstract BigDecimal getRoundedAmount();

    @JsonIgnore
    @Override
    public abstract Amount getAmount();
}
