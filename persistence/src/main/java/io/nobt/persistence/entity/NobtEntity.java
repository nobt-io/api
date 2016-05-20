package io.nobt.persistence.entity;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Table(name = "nobts")
@Entity
public class NobtEntity extends AbstractEntity {

	@Column(name = "uuid", nullable = false, unique = true, length = 50)
	private UUID uuid;

	@Column(name = "nobtName", nullable = false, unique = true, length = 50)
	private String name;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "nobt", cascade = CascadeType.ALL)
	private Set<ExpenseEntity> expenses = new HashSet<>();

	public NobtEntity(String name, UUID uuid) {
		this.name = name;
		this.uuid = uuid;
	}

	public NobtEntity() {

	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<ExpenseEntity> getExpenses() {
		return expenses;
	}

	public void setExpenses(Set<ExpenseEntity> expenses) {
		this.expenses = expenses;
	}

	public void addExpense(ExpenseEntity expense) {
		if (expenses == null) {
			expenses = new HashSet<>();
		}
		expenses.add(expense);
		expense.setNobt(this);
	}

}
