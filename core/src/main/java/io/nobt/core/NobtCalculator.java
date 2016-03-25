/**
 * 
 */
package io.nobt.core;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import io.nobt.core.domain.Expense;
import io.nobt.core.domain.Nobt;
import io.nobt.core.domain.Person;

/**
 * @author Matthias
 *
 */
public class NobtCalculator {

	private static final int SCALE = 2;

	public Set<Transaction> calculate(Nobt nobt) {

		List<Transaction> expenseTransactions = new ArrayList<>();

		for (Expense expense : nobt.getExpenses()) {
			Person debtee = expense.getDebtee();

			BigDecimal amountPerDebtor = expense.getAmount().divide(new BigDecimal(expense.getDebtors().size()), SCALE,
					RoundingMode.HALF_UP);

			expenseTransactions.addAll(expense.getDebtors().stream()
					.map(debtor -> Transaction.transaction(debtor, Amount.fromBigDecimal(amountPerDebtor), debtee))
					.collect(Collectors.toList()));
		}

		TransactionListOptimizer optimizer = new TransactionListOptimizer(expenseTransactions);
		List<Transaction> optimalTransactions = optimizer.getOptimalTransactions();

		return new HashSet<>(optimalTransactions);
	}
}
