package io.nobt.core;

import io.nobt.core.domain.Nobt;
import io.nobt.core.domain.Transaction;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NobtCalculator {

    public Set<Transaction> calculate(Nobt nobt) {

        final List<Transaction> allTransactions = nobt.getAllTransactions();

        TransactionListOptimizer optimizer = new TransactionListOptimizer(allTransactions);
        List<Transaction> optimalTransactions = optimizer.getOptimalTransactions();

        return new HashSet<>(optimalTransactions);
    }
}
