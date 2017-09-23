package io.nobt.core.optimizer;

import io.nobt.core.domain.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static java.util.Comparator.comparingInt;

public class SelfSortingOptimizerStrategy implements OptimizerStrategy {

	@Override
	public List<Transaction> optimize(List<Transaction> unoptimizedTransactions) {
		return new Optimizer(unoptimizedTransactions).getOptimalTransactions();
	}

	private static class Optimizer {

		private List<Transaction> transactions;
		private boolean needsFurtherOptimization;

		public Optimizer(List<Transaction> transactions) {
			this.transactions = new ArrayList<>(transactions);
			this.transactions.sort(comparingInt(Transaction::hashCode));
		}

		public List<Transaction> getOptimalTransactions() {
			do {
				transactions = tryOptimize(transactions);
			} while (needsFurtherOptimization);

			return transactions;
		}

		List<Transaction> tryOptimize(List<Transaction> expenseTransactions) {
			needsFurtherOptimization = false;

			List<Transaction> copy = new ArrayList<>(expenseTransactions);

			for (Transaction first : copy) {
				for (Transaction second : copy) {

					if (first == second) {
						continue;
					}

					Set<Transaction> result = first.combine(second);

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

		private boolean anyChanges(Transaction first, Transaction second, Set<Transaction> result) {
			return !(result.size() == 2 && result.contains(first) && result.contains(second));
		}
	}
}
