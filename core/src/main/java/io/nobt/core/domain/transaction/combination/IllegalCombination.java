package io.nobt.core.domain.transaction.combination;

import io.nobt.core.domain.transaction.Transaction;

import java.util.Collection;

public class IllegalCombination implements CombinationResult {

    @Override
    public boolean hasChanges() {
        return true;
    }

    @Override
    public void applyTo(Collection<Transaction> transactions) {
        throw new IllegalStateException("Illegal combination of transactions!");
    }

    @Override
    public boolean equals(Object obj) {
        return this.getClass().equals(obj.getClass());
    }
}
