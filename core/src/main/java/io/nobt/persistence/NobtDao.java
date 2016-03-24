package io.nobt.persistence;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

import io.nobt.core.domain.Expense;
import io.nobt.core.domain.Nobt;
import io.nobt.core.domain.Person;

public interface NobtDao {

	Nobt create(String nobtName);

	Expense createExpense(UUID nobtId, String name, BigDecimal amount, Person debtee, Set<Person> debtors);

}
