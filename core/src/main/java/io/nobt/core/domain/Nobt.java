package io.nobt.core.domain;

import java.util.HashSet;
import java.util.Set;

public class Nobt {

	private String name;

	private Set<Expense> expenses = new HashSet<>();

	public Nobt(String name) {
		this.name = name;
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
}
