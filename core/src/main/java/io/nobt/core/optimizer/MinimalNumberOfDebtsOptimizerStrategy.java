package io.nobt.core.optimizer;

import io.nobt.core.domain.debt.Debt;

import java.util.List;

public class MinimalNumberOfDebtsOptimizerStrategy implements OptimizerStrategy {

    @Override
    public List<Debt> optimize(List<Debt> debts) {
        return new MinimalNumberOfDebtsOptimizer(debts).getMinimalNumberOfDebts();
    }

}