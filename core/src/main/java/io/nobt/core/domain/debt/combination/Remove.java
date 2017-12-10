package io.nobt.core.domain.debt.combination;

import io.nobt.core.domain.debt.Debt;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

public final class Remove implements CombinationResult {

    private final Collection<Debt> debtsToBeRemoved;

    public Remove(Debt... debtsToBeRemoved) {
        this.debtsToBeRemoved = Arrays.asList(debtsToBeRemoved);
    }

    @Override
    public boolean hasChanges() {
        return true;
    }

    @Override
    public void applyTo(Collection<Debt> existingDebts) {

        for (Debt debtToBeRemoved : debtsToBeRemoved) {
            existingDebts.remove(debtToBeRemoved);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Remove remove = (Remove) o;
        return debtsToBeRemoved.containsAll(remove.debtsToBeRemoved) &&
                remove.debtsToBeRemoved.containsAll(debtsToBeRemoved);
    }

    @Override
    public int hashCode() {
        return Objects.hash(debtsToBeRemoved);
    }

    @Override
    public String toString() {
        return String.format("Remove{%s}", debtsToBeRemoved);
    }
}
