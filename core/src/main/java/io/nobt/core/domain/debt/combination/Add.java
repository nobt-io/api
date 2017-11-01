package io.nobt.core.domain.debt.combination;

import io.nobt.core.domain.debt.Debt;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

public final class Add implements CombinationResult {

    private final Collection<Debt> transactionsToBeAdded;

    public Add(Debt... transactionsToBeAdded) {
        this.transactionsToBeAdded = Arrays.asList(transactionsToBeAdded);
    }

    @Override
    public boolean hasChanges() {
        return true;
    }

    @Override
    public void applyTo(Collection<Debt> debts) {
        debts.addAll(transactionsToBeAdded);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Add add = (Add) o;
        return transactionsToBeAdded.containsAll(add.transactionsToBeAdded) &&
                add.transactionsToBeAdded.containsAll(transactionsToBeAdded);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionsToBeAdded);
    }

    @Override
    public String toString() {
        return String.format("Add{%s}", transactionsToBeAdded);
    }
}
