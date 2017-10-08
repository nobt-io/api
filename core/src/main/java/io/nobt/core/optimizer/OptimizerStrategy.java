package io.nobt.core.optimizer;

import io.nobt.core.domain.transaction.Transaction;

import java.util.List;

public interface OptimizerStrategy {

	List<Transaction> optimize(List<Transaction> unoptimizedTransactions);

}
