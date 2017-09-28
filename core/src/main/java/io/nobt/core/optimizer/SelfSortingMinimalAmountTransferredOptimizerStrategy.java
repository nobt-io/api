package io.nobt.core.optimizer;

import io.nobt.core.domain.Debt;

import java.util.ArrayList;
import java.util.List;

import static java.util.Comparator.comparingInt;

public class SelfSortingMinimalAmountTransferredOptimizerStrategy implements OptimizerStrategy {

    @Override
    public List<Debt> optimize(List<Debt> debts) {

        final ArrayList<Debt> copy = new ArrayList<>(debts);
        copy.sort(comparingInt(Debt::hashCode));

        return new MinimalAmountTransferredOptimizer(copy).getDebtsWithMinimalAmountTransferred();
    }

}
