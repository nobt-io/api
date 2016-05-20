package io.nobt.persistence.dao;

import static java.util.stream.Collectors.toList;

import java.util.stream.Collectors;

import io.nobt.core.domain.Amount;
import io.nobt.core.domain.Expense;
import io.nobt.core.domain.Nobt;
import io.nobt.core.domain.Person;
import io.nobt.persistence.entity.ExpenseEntity;
import io.nobt.persistence.entity.NobtEntity;
import io.nobt.persistence.entity.PersonEntity;

public class NobtMapper {

	public Nobt map(NobtEntity entity) {
		Nobt nobt = new Nobt(entity.getName(), entity.getUuid());
		nobt.getExpenses().addAll(entity.getExpenses().stream().map(exp -> map(exp)).collect(toList()));
		return nobt;
	}

	public Expense map(ExpenseEntity entity) {
		Expense expense = new Expense(entity.getName(), Amount.fromBigDecimal(entity.getAmount()),
				map(entity.getDebtee()));
		expense.getDebtors().addAll(
				entity.getDebtors().stream().map(p -> Person.forName(p.getName())).collect(Collectors.toList()));
		return expense;
	}

	private Person map(PersonEntity entity) {
		return new Person(entity.getName());
	}

}
