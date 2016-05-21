package io.nobt.persistence.entity;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author Matthias
 *
 */
@Table(name = "expenses")
@Entity
public class ExpenseEntity extends AbstractEntity {

	@Column(name = "expenseName", nullable = false, length = 50)
	private String name;

	@Column(name = "amount", nullable = false)
	private BigDecimal amount;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "DEBTEE_ID", nullable = false)
	private PersonEntity debtee;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "NOBT_ID", nullable = false)
	private NobtEntity nobt;

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "expense_debtors", joinColumns = {
			@JoinColumn(name = "EXPENSE_ID", nullable = false, updatable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "PERSON_ID", nullable = false, updatable = false) })
	private Set<PersonEntity> debtors = new HashSet<PersonEntity>();

	public ExpenseEntity(String name, BigDecimal amount, PersonEntity debtee) {
		this.name = name;
		this.amount = amount;
		this.debtee = debtee;
		debtee.getExpenses().add(this);
	}

	public ExpenseEntity() {

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public PersonEntity getDebtee() {
		return debtee;
	}

	public void setDebtee(PersonEntity debtee) {
		this.debtee = debtee;
	}

	public NobtEntity getNobt() {
		return nobt;
	}

	public void setNobt(NobtEntity nobt) {
		this.nobt = nobt;
	}

	public Set<PersonEntity> getDebtors() {
		return debtors;
	}

	public void setDebtors(Set<PersonEntity> debtors) {
		this.debtors = debtors;
	}

}