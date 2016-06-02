package io.nobt.persistence.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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

	@Column(name = "debtee")
	private String debtee;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "NOBT_ID", nullable = false)
	private NobtEntity nobt;

	@Column(name = "debtors")
	private String debtorList;

	public ExpenseEntity(String name, BigDecimal amount, String debtee) {
		this.name = name;
		this.amount = amount;
		this.debtee = debtee;
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

	public String getDebtee() {
		return debtee;
	}

	public void setDebtee(String debtee) {
		this.debtee = debtee;
	}

	public NobtEntity getNobt() {
		return nobt;
	}

	public void setNobt(NobtEntity nobt) {
		this.nobt = nobt;
	}

	public List<String> getDebtors() {

		if (debtorList == null) {
			return Collections.emptyList();
		}

		return Arrays.asList(debtorList.split(";"));
	}

	public void addDebtor(String debtor) {
		if (debtorList == null) {
			debtorList = debtor;
		} else {
			debtorList = debtorList + ";" + debtor;
		}
	}
}