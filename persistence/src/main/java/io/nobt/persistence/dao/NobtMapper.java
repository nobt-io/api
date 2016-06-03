package io.nobt.persistence.dao;

import io.nobt.core.domain.Amount;
import io.nobt.core.domain.Expense;
import io.nobt.core.domain.Nobt;
import io.nobt.core.domain.Person;
import io.nobt.persistence.entity.ExpenseEntity;
import io.nobt.persistence.entity.NobtEntity;

import static java.util.stream.Collectors.toList;

public class NobtMapper {

	public Nobt map(NobtEntity entity) {
		Nobt nobt = new Nobt(entity.getName(), entity.getUuid());
		nobt.getExpenses().addAll(entity.getExpenses().stream().map(this::map).collect(toList()));
		return nobt;
	}

	public Expense map(ExpenseEntity entity) {

		Expense expense = new Expense(entity.getName(), Amount.fromBigDecimal(entity.getAmount()), Person.forName(entity.getDebtee()));
		expense.getDebtors().addAll(entity.getDebtors().stream().map(Person::forName).collect(toList()));

		return expense;
	}
}
