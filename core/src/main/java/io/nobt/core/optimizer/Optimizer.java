package io.nobt.core.optimizer;

import io.nobt.core.domain.Debt;

import java.util.List;

public enum Optimizer {

	MINIMAL_AMOUNT_V1(new SelfSortingMinimalAmountTransferredOptimizerStrategy());

	private final OptimizerStrategy strategy;

	Optimizer(OptimizerStrategy strategy) {
		this.strategy = strategy;
	}

	public List<Debt> apply(List<Debt> debts) {
		return strategy.optimize(debts);
	}

	public static Optimizer defaultOptimizer() {
		return MINIMAL_AMOUNT_V1;
	}
}
