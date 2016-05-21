package io.nobt.persistence.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Table(name = "persons")
@Entity
public class PersonEntity extends AbstractEntity {

	@Column(name = "personName", nullable = false, length = 50)
	private String name;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "debtee")
	private Set<ExpenseEntity> expenses = new HashSet<>();

	public PersonEntity() {

	}

	public PersonEntity(String name) {
		this.name = name;
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

}
