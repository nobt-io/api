package io.nobt.core.domain;

import io.nobt.core.optimizer.Optimizer;

import java.util.ArrayList;
import java.util.List;

import static io.nobt.core.domain.Debt.debt;

public class Debts {

    private final Optimizer optimizer;
    private final List<Debt> debts;

    public Debts(Optimizer optimizer) {
        this.optimizer = optimizer;
        debts = new ArrayList<>();
    }

    public void addDebt(Person from, Amount amount, Person to) {
        debts.add(debt(from, amount, to));
    }

    public List<Debt> asTransactionList() {
        return optimizer.apply(debts);
    }
}
