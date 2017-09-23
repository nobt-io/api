package io.nobt.core.optimizer;

import io.nobt.core.domain.Transaction;

import java.util.List;

public enum Optimizer {

	MINIMAL_AMOUNT_V1(new SelfSortingOptimizerStrategy());

	private final OptimizerStrategy strategy;

	Optimizer(OptimizerStrategy strategy) {
		this.strategy = strategy;
	}

	public List<Transaction> apply(List<Transaction> unoptimizedTransactions) {
		return strategy.optimize(unoptimizedTransactions);
	}
}
