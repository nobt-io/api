/**
 * 
 */
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

	private BigDecimal amount;

	private Person debtee;

	private Set<Person> debtors = new HashSet<Person>();

	public Expense(String name, BigDecimal amount, Person debtee) {
		super();
		this.name = name;
		this.amount = amount;
		this.debtee = debtee;
	}

	public String getName() {
		return name;
	}

	public BigDecimal getAmount() {
		return amount;
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
	public int hashCode() {
		return Objects.hash(name, amount, debtee);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Expense other = (Expense) obj;
		if (amount == null) {
			if (other.amount != null)
				return false;
		} else if (!amount.equals(other.amount))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (debtee == null) {
			if (other.debtee != null)
				return false;
		} else if (!debtee.equals(other.debtee))
			return false;
		return true;
	}
}
