package io.nobt.core.domain.transaction.combination;

import io.nobt.core.domain.transaction.Debt;

import java.util.Collection;

public interface CombinationResult {

    boolean hasChanges();

    void applyTo(Collection<Debt> debts);

}
