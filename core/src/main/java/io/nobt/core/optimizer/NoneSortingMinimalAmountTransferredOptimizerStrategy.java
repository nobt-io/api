package io.nobt.core.optimizer;

import io.nobt.core.domain.transaction.Debt;

import java.util.List;

public class NoneSortingMinimalAmountTransferredOptimizerStrategy implements OptimizerStrategy {
    @Override
    public List<Debt> optimize(List<Debt> debts) {
        return new MinimalAmountTransferredOptimizer(debts).getDebtsWithMinimalAmountTransferred();
    }
}
