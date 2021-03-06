package io.nobt.core.domain.debt.combination;

import io.nobt.core.domain.debt.Debt;

import java.util.Collection;

public class IllegalCombination implements CombinationResult {

    @Override
    public boolean hasChanges() {
        return true;
    }

    @Override
    public void applyTo(Collection<Debt> debts) {
        throw new IllegalStateException("Illegal combination of transactions!");
    }

    @Override
    public boolean equals(Object obj) {
        return this.getClass().equals(obj.getClass());
    }
}
