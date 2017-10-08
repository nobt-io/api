package io.nobt.core.domain;

import io.nobt.core.domain.transaction.Debt;
import io.nobt.core.domain.transaction.combination.CombinationResult;

import java.util.Collection;

public class HasNoChangesResult implements CombinationResult {
    @Override
    public boolean hasChanges() {
        return false;
    }

    @Override
    public void applyTo(Collection<Debt> debts) {

    }
}
