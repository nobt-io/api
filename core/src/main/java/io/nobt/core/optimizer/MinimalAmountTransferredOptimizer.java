package io.nobt.core.optimizer;

import io.nobt.core.domain.Debt;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

                if (first == second) {
                    continue;
                }

                Set<Debt> result = first.combine(second);

                if (anyChanges(first, second, result)) {
                    copy.remove(first);
                    copy.remove(second);
                    copy.addAll(result);

                    needsFurtherOptimization = true;

                    return copy;
                }
            }
        }

        return debts;
    }

    private boolean anyChanges(Debt first, Debt second, Set<Debt> result) {
        return !(result.size() == 2 && result.contains(first) && result.contains(second));
    }
}
