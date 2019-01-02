package io.nobt.core.optimizer;

import io.nobt.core.domain.debt.Debt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OptimizerStrategyPipeline implements OptimizerStrategy {

    private final List<OptimizerStrategy> strategies;

    private OptimizerStrategyPipeline(List<OptimizerStrategy> strategies) {
        this.strategies = strategies;
    }

    public static OptimizerStrategy applyInOrder(OptimizerStrategy... strategies) {
        return new OptimizerStrategyPipeline(Arrays.asList(strategies));
    }

    @Override
    public List<Debt> optimize(List<Debt> debts) {

        List<Debt> localCopy = new ArrayList<>(debts);

        for (OptimizerStrategy strategy : strategies) {
            localCopy = strategy.optimize(localCopy);
        }

        return localCopy;
    }
}
