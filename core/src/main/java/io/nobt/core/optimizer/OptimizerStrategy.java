package io.nobt.core.optimizer;

import io.nobt.core.domain.debt.Debt;

import java.util.List;

public interface OptimizerStrategy {

	List<Debt> optimize(List<Debt> debts);

}
