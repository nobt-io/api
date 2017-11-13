package io.nobt.core.domain.transaction.combination;

import io.nobt.core.domain.transaction.Transaction;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

public final class Remove implements CombinationResult {

    private final Collection<Transaction> transactionsToBeRemoved;

    public Remove(Transaction... transactionsToBeRemoved) {
        this.transactionsToBeRemoved = Arrays.asList(transactionsToBeRemoved);
    }

    @Override
    public boolean hasChanges() {
        return true;
    }

    @Override
    public void applyTo(Collection<Transaction> existingTransactions) {

        for (Transaction transactionToBeRemoved : transactionsToBeRemoved) {
            existingTransactions.remove(transactionToBeRemoved);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Remove remove = (Remove) o;
        return transactionsToBeRemoved.containsAll(remove.transactionsToBeRemoved) &&
                remove.transactionsToBeRemoved.containsAll(transactionsToBeRemoved);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionsToBeRemoved);
    }

    @Override
    public String toString() {
        return String.format("Remove{%s}", transactionsToBeRemoved);
    }
}
