package io.nobt.core.domain.debt.combination;

import io.nobt.core.domain.debt.Debt;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

public final class Add implements CombinationResult {

    private final Collection<Debt> debtsToBeAdded;

    public Add(Debt... debtsToBeAdded) {
        this.debtsToBeAdded = Arrays.asList(debtsToBeAdded);
    }

    @Override
    public boolean hasChanges() {
        return true;
    }

    @Override
    public void applyTo(Collection<Debt> debts) {
        debts.addAll(debtsToBeAdded);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Add add = (Add) o;
        return debtsToBeAdded.containsAll(add.debtsToBeAdded) &&
                add.debtsToBeAdded.containsAll(debtsToBeAdded);
    }

    @Override
    public int hashCode() {
        return Objects.hash(debtsToBeAdded);
    }

    @Override
    public String toString() {
        return String.format("Add{%s}", debtsToBeAdded);
    }
}
