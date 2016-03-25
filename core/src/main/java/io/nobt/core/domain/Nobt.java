package io.nobt.core.domain;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Nobt {

	private UUID id;

	private String name;

	private Set<Expense> expenses = new HashSet<>();

	public Nobt(String name, UUID uuid) {
		this.name = name;
		this.id = uuid;
	}

	public String getName() {
		return name;
	}

	public Set<Expense> getExpenses() {
		return expenses;
	}

	public Set<Person> getParticipatingPersons() {
		final Set<Person> participatingPersons = expenses
				.stream()
				.map(Expense::getDebtors)
				.collect(HashSet::new, HashSet::addAll, HashSet::addAll);

		expenses.stream().map(Expense::getDebtee).forEach(participatingPersons::add);

		return participatingPersons;
	}

	public void addExpense(Expense expense) {
		this.expenses.add(expense);
	}

	public UUID getId() {
		return id;
	}

}
