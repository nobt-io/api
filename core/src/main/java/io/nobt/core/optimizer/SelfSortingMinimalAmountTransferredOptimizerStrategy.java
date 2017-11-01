package io.nobt.core.optimizer;

import io.nobt.core.domain.debt.Debt;

import java.util.List;

import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.toList;

public class SelfSortingMinimalAmountTransferredOptimizerStrategy implements OptimizerStrategy {

    @Override
    public List<Debt> optimize(List<Debt> debts) {

        final List<Debt> sortedCopy = debts
                .stream()
                .sorted(comparingInt(Debt::hashCode))
                .collect(toList());

        return new MinimalAmountTransferredOptimizer(sortedCopy).getDebtsWithMinimalAmountTransferred();
    }

}
