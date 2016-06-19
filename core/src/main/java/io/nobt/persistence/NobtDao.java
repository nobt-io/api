package io.nobt.persistence;

import io.nobt.core.domain.Expense;
import io.nobt.core.domain.Nobt;
import io.nobt.core.domain.Person;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

public interface NobtDao {

	Nobt create(String nobtName);

	Expense createExpense(UUID nobtId, String name, BigDecimal amount, Person debtee, Set<Person> debtors);

	Nobt find(UUID nobtId);

}
