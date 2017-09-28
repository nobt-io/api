package io.nobt.core.optimizer;

import io.nobt.core.domain.Debt;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static java.util.Comparator.comparingInt;

public class SelfSortingOptimizerStrategy implements OptimizerStrategy {

	@Override
	public List<Debt> optimize(List<Debt> unoptimizedTransactions) {
		return new Optimizer(unoptimizedTransactions).getOptimalTransactions();
	}

	private static class Optimizer {

		private List<Debt> transactions;
		private boolean needsFurtherOptimization;

		public Optimizer(List<Debt> transactions) {
			this.transactions = new ArrayList<>(transactions);
			this.transactions.sort(comparingInt(Debt::hashCode));
		}

		public List<Debt> getOptimalTransactions() {
			do {
				transactions = tryOptimize(transactions);
			} while (needsFurtherOptimization);

			return transactions;
		}

		List<Debt> tryOptimize(List<Debt> expenseTransactions) {
			needsFurtherOptimization = false;

			List<Debt> copy = new ArrayList<>(expenseTransactions);

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

			return expenseTransactions;
		}

		private boolean anyChanges(Debt first, Debt second, Set<Debt> result) {
			return !(result.size() == 2 && result.contains(first) && result.contains(second));
		}
	}
}
