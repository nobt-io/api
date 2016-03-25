/**
 * 
 */
package io.nobt.persistence.dao;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import io.nobt.core.domain.Amount;
import io.nobt.core.domain.Expense;
import io.nobt.core.domain.Nobt;
import io.nobt.core.domain.Person;
import io.nobt.persistence.NobtDao;

/**
 * @author Matthias
 *
 */
public class InMemoryNobtDao implements NobtDao {

	private static final Map<UUID, Nobt> nobtDatabase = new HashMap<>();

	@Override
	public Nobt create(String nobtName) {
		Nobt nobt = new Nobt(nobtName);
		nobtDatabase.put(nobt.getId(), nobt);
		return nobt;
	}

	@Override
	public Expense createExpense(UUID nobtId, String name, BigDecimal amount, Person debtee, Set<Person> debtors) {

		Nobt nobt = nobtDatabase.get(nobtId);

		Expense expense = new Expense(name, Amount.fromBigDecimal(amount), debtee);
		expense.setDebtors(debtors);

		nobt.addExpense(expense);
		return expense;
	}

	@Override
	public Nobt find(UUID nobtId) {
		return nobtDatabase.get(nobtId);
	}

}
