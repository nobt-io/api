package io.nobt.core.domain.transaction.combination;

import io.nobt.core.domain.transaction.Transaction;

import java.util.Collection;

public class NotCombinable implements CombinationResult {

    @Override
    public boolean hasChanges() {
        return false;
    }

    @Override
    public void applyTo(Collection<Transaction> transactions) {

    }

    @Override
    public boolean equals(Object obj) {
        return this.getClass().equals(obj.getClass());
    }
}
