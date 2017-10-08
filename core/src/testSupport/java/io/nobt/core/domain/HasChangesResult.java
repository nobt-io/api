package io.nobt.core.domain;

import io.nobt.core.domain.transaction.Transaction;
import io.nobt.core.domain.transaction.combination.CombinationResult;

import java.util.Collection;

public class HasChangesResult implements CombinationResult {
    @Override
    public boolean hasChanges() {
        return true;
    }

    @Override
    public void applyTo(Collection<Transaction> transactions) {

    }
}
