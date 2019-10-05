package io.nobt.rest.json.debt;

import io.nobt.core.domain.Amount;
import io.nobt.core.domain.Person;
import io.nobt.core.domain.debt.Debt;

public abstract class DebtMixin extends Debt {

    protected DebtMixin(Person debtor, Amount amount, Person debtee) {
        super(debtor, amount, debtee);
    }
}
