package io.nobt.core.optimizer;

public enum OptimizerVersion {

	V1(new SelfSortingOptimizerStrategy());

	private final OptimizerStrategy strategy;

	OptimizerVersion(OptimizerStrategy strategy) {
		this.strategy = strategy;
	}

	public OptimizerStrategy getStrategy() {
		return strategy;
	}
}
