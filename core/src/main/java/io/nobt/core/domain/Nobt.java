package io.nobt.core.domain;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Nobt {

	private UUID id;

	private String name;

	private Set<Expense> expenses = new HashSet<>();

	public Nobt(String name) {
		this.name = name;
		this.id = UUID.randomUUID();
	}

	public String getName() {
		return name;
	}

	public Set<Expense> getExpenses() {
		return expenses;
	}

	public void addExpense(Expense expense) {
		this.expenses.add(expense);
	}

	public UUID getId() {
		return id;
	}

}
