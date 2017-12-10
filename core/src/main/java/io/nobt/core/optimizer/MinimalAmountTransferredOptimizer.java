package io.nobt.core.optimizer;

import io.nobt.core.domain.debt.Debt;
import io.nobt.core.domain.debt.combination.CombinationResult;

import java.util.ArrayList;
import java.util.List;

public class MinimalAmountTransferredOptimizer {

    private List<Debt> debts;
    private boolean needsFurtherOptimization;

    public MinimalAmountTransferredOptimizer(List<Debt> debts) {
        this.debts = new ArrayList<>(debts);
    }

    public List<Debt> getDebtsWithMinimalAmountTransferred() {
        do {
            debts = optimize(debts);
        } while (needsFurtherOptimization);

        return debts;
    }

    private List<Debt> optimize(List<Debt> debts) {
        needsFurtherOptimization = false;

        List<Debt> copy = new ArrayList<>(debts);

        for (Debt first : copy) {
            for (Debt second : copy) {

                final CombinationResult result = first.combine(second);

                if (result.hasChanges()) {
                    result.applyTo(copy);

                    needsFurtherOptimization = true;

                    return copy;
                }
            }
        }

        return debts;
    }

}
