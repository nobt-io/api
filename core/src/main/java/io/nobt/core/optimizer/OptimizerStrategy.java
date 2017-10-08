package io.nobt.core.optimizer;

import io.nobt.core.domain.transaction.Debt;

import java.util.List;

public interface OptimizerStrategy {

	List<Debt> optimize(List<Debt> debts);

}
