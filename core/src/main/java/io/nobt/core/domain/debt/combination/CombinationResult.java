package io.nobt.core.domain.debt.combination;

import io.nobt.core.domain.debt.Debt;

import java.util.Collection;

public interface CombinationResult {

    boolean hasChanges();

    void applyTo(Collection<Debt> debts);

}
