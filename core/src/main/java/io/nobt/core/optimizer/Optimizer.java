package io.nobt.core.optimizer;

import io.nobt.core.domain.debt.Debt;

import java.util.List;

public enum Optimizer {

    MINIMAL_AMOUNT_V1(new SelfSortingMinimalAmountTransferredOptimizerStrategy()),
    MINIMAL_AMOUNT_V2(new NoneSortingMinimalAmountTransferredOptimizerStrategy()),
    MINIMAL_AMOUNTS_AND_MINIMAL_NUMBER_OF_DEBTS(OptimizerStrategyPipeline.applyInOrder(
            new NoneSortingMinimalAmountTransferredOptimizerStrategy(),
            new MinimalNumberOfDebtsOptimizerStrategy()
    ));

    private final OptimizerStrategy strategy;

    Optimizer(OptimizerStrategy strategy) {
        this.strategy = strategy;
    }

    public static Optimizer defaultOptimizer() {
        return MINIMAL_AMOUNTS_AND_MINIMAL_NUMBER_OF_DEBTS;
    }

    public List<Debt> apply(List<Debt> debts) {
        return strategy.optimize(debts);
    }
}
