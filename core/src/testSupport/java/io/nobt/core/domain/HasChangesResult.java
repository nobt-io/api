package io.nobt.core.domain;

import io.nobt.core.domain.debt.Debt;
import io.nobt.core.domain.debt.combination.CombinationResult;

import java.util.Collection;

public class HasChangesResult implements CombinationResult {
    @Override
    public boolean hasChanges() {
        return true;
    }

    @Override
    public void applyTo(Collection<Debt> debts) {

    }
}
