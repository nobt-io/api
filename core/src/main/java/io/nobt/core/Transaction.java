package io.nobt.core;

import static io.nobt.core.domain.Person.personByName;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import io.nobt.core.domain.Person;

public class Transaction {

	/**
	 * Gives money.
	 */
	private Person debtor;
	private BigDecimal amount;

	/**
	 * Receives money.
	 */
	private Person debtee;

	private Transaction(Person debtor, BigDecimal amount, Person debtee) {
		this.debtor = debtor;
		this.amount = amount;
		this.debtee = debtee;
	}

	public static Transaction transaction(String debtor, double amount, String debtee) {
		return transaction(personByName(debtor), new BigDecimal(amount).setScale(10, RoundingMode.HALF_UP),
				personByName(debtee));
	}

	public static Transaction transaction(Person debtor, BigDecimal amount, Person debtee) {
		boolean isNotPositive = amount.signum() != 1;
		if (isNotPositive) {
			throw new IllegalArgumentException("amount must be greater than zero.");
		}
		return new Transaction(debtor, amount, debtee);
	}

	public Set<Transaction> combine(Transaction other) {

		HashSet<Person> participatingPersons = new HashSet<>(Arrays.asList(debtor, debtee, other.debtor, other.debtee));

		boolean fourDifferentPersons = participatingPersons.size() == 4;
		boolean sameDebtorWithDifferentDebtees = debtor.equals(other.debtor) && !debtee.equals(other.debtee);
		boolean sameDebteeWithDifferentDebtors = debtee.equals(other.debtee) && !debtor.equals(other.debtor);
		boolean twoPersonsInDebtWithEachOther = debtor.equals(other.debtee) && debtee.equals(other.debtor);
		boolean threePersonsWithSameDebteeAndDebtor = debtee.equals(other.debtor);
		boolean threePersonsWithSameDebteeAndDebtorReversed = debtor.equals(other.debtee);
		boolean sameTransactionWithDifferentAmount = debtor.equals(other.debtor) && debtee.equals(other.debtee);

		if (fourDifferentPersons || sameDebtorWithDifferentDebtees || sameDebteeWithDifferentDebtors) {
			return transactions(this, other);
		}

		if (twoPersonsInDebtWithEachOther) {

			BigDecimal remainingAmount = amount.subtract(other.amount);

			switch (remainingAmount.signum()) {
			case -1:
				return transactions(new Transaction(debtee, remainingAmount.abs(), debtor));
			case 0:
				return transactions();
			case +1:
				return transactions(new Transaction(debtor, remainingAmount.abs(), debtee));
			}
		}

		if (threePersonsWithSameDebteeAndDebtor) {

			BigDecimal remainingAmount = amount.subtract(other.amount);

			switch (remainingAmount.signum()) {
			case -1:
				return transactions(new Transaction(debtor, amount, other.debtee),
						new Transaction(debtee, remainingAmount.abs(), other.debtee));
			case 0:
				return transactions(new Transaction(debtor, amount, other.debtee));
			case +1:
				return transactions(new Transaction(debtor, other.amount, other.debtee),
						new Transaction(debtor, remainingAmount.abs(), debtee));
			}
		}

		if (threePersonsWithSameDebteeAndDebtorReversed) {

			BigDecimal remainingAmount = amount.subtract(other.amount);

			switch (remainingAmount.signum()) {
			case -1:
				return transactions(new Transaction(other.debtor, remainingAmount.abs(), other.debtee),
						new Transaction(other.debtor, amount, debtee));
			case 0:
				return transactions(new Transaction(other.debtor, amount, debtee));
			case +1:
				return transactions(new Transaction(debtor, remainingAmount.abs(), debtee),
						new Transaction(other.debtor, other.amount, debtee));
			}
		}

		if (sameTransactionWithDifferentAmount) {
			return transactions(new Transaction(debtor, amount.add(other.amount), other.debtee));
		}

		throw new IllegalStateException("Should not reach here");
	}

	private Set<Transaction> transactions(Transaction... transactions) {
		return new HashSet<>(Arrays.asList(transactions));
	};

	public Person getDebtor() {
		return debtor;
	}

	public BigDecimal getAmount() {
		return amount.setScale(2, RoundingMode.HALF_UP);
	}

	public Person getDebtee() {
		return debtee;
	}

	@Override
	public int hashCode() {
		return Objects.hash(debtor, amount, debtee);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Transaction other = (Transaction) obj;
		if (amount == null) {
			if (other.amount != null)
				return false;
		} else if (!amount.equals(other.amount))
			return false;
		if (debtee == null) {
			if (other.debtee != null)
				return false;
		} else if (!debtee.equals(other.debtee))
			return false;
		if (debtor == null) {
			if (other.debtor != null)
				return false;
		} else if (!debtor.equals(other.debtor))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return String.format("%s -> %s: %s", debtor, debtee, amount);
	}

}
