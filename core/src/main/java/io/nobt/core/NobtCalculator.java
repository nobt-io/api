package io.nobt.core;

import io.nobt.core.domain.Nobt;
import io.nobt.core.domain.Transaction;
import io.nobt.core.optimizer.OptimizerStrategy;
import io.nobt.core.optimizer.OptimizerVersion;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NobtCalculator {

    public Set<Transaction> calculate(Nobt nobt) {

        final List<Transaction> allTransactions = nobt.getAllTransactions();
        final OptimizerStrategy optimizerStrategy = nobt.getOptimizerVersion().getStrategy();

        final List<Transaction> optimalTransactions = optimizerStrategy.optimize(allTransactions);

        return new HashSet<>(optimalTransactions);
    }
}
