package io.nobt.core.domain;

import io.nobt.core.domain.debt.Debt;
import io.nobt.core.domain.debt.combination.CombinationResult;

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
