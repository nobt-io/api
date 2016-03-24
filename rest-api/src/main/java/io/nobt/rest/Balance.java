package io.nobt.rest;

import java.math.BigDecimal;
import java.util.Map;

import io.nobt.core.domain.Person;

public class Balance {

	private Person owner;

	private Map<Person, BigDecimal> transactions;

	public Balance(Person owner, Map<Person, BigDecimal> transactions) {
		super();
		this.owner = owner;
		this.transactions = transactions;
	}

	public Person getOwner() {
		return owner;
	}

	public Map<Person, BigDecimal> getTransactions() {
		return transactions;
	}
}
