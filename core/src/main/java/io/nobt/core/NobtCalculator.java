/**
 * 
 */
package io.nobt.core;

import io.nobt.core.domain.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

/**
 * @author Matthias
 *
 */
public class NobtCalculator {

	public Set<Transaction> calculate(Nobt nobt) {

		List<Transaction> expenseTransactions = new ArrayList<>();

		for (Expense expense : nobt.getExpenses()) {
			Person debtee = expense.getDebtee();

			Amount amountPerDebtor = expense.getAmountPerDebtor();

			expenseTransactions.addAll(expense.getDebtors().stream()
					.map(debtor -> Transaction.transaction(debtor, amountPerDebtor, debtee))
					.collect(toList()));
		}

		TransactionListOptimizer optimizer = new TransactionListOptimizer(expenseTransactions);
		List<Transaction> optimalTransactions = optimizer.getOptimalTransactions();

		return new HashSet<>(optimalTransactions);
	}
}
