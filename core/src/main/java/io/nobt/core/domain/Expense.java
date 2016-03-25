package io.nobt.core.domain;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author Matthias
 *
 */
public class Expense {

	private String name;
	private Amount amount;
	private Person debtee;
	private Set<Person> debtors = new HashSet<Person>();

	public Expense(String name, Amount amount, Person debtee) {
		this.name = name;
		this.amount = amount;
		this.debtee = debtee;
	}

	public String getName() {
		return name;
	}

	public Amount getOverallAmount() {
		return amount;
	}

	public Amount getAmountPerDebtor() {
		return amount.divide(new BigDecimal(debtors.size()));
	}

	public Person getDebtee() {
		return debtee;
	}

	public Set<Person> getDebtors() {
		return debtors;
	}

	public void setDebtors(Set<Person> debtors) {
		this.debtors = debtors;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Expense)) return false;
		Expense expense = (Expense) o;
		return Objects.equals(name, expense.name) &&
				Objects.equals(amount, expense.amount) &&
				Objects.equals(debtee, expense.debtee) &&
				Objects.equals(debtors, expense.debtors);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, amount, debtee, debtors);
	}
}
