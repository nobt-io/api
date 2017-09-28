package io.nobt.core.domain;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static io.nobt.core.domain.Person.forName;

public class Debt {

	/**
	 * Owes money.
	 */
	private Person debtor;
	private Amount amount;

	/**
	 * Receives money.
	 */
	private Person debtee;

	private Debt(Person debtor, Amount amount, Person debtee) {
		this.debtor = debtor;
		this.amount = amount;
		this.debtee = debtee;
	}

	@Deprecated
	public static Debt debt(String debtor, double amount, String debtee) {
		return debt(forName(debtor), Amount.fromDouble(amount), forName(debtee));
	}

	public static Debt debt(Person debtor, Amount amount, Person debtee) {
		if (!amount.isPositive()) {
			throw new IllegalArgumentException("Amount must be positive.");
		}
		return new Debt(debtor, amount, debtee);
	}

	public Set<Debt> combine(Debt other) {

		HashSet<Person> participatingPersons = new HashSet<>(Arrays.asList(debtor, debtee, other.debtor, other.debtee));

		boolean fourDifferentPersons = participatingPersons.size() == 4;
		boolean sameDebtorWithDifferentDebtees = debtor.equals(other.debtor) && !debtee.equals(other.debtee);
		boolean sameDebteeWithDifferentDebtors = debtee.equals(other.debtee) && !debtor.equals(other.debtor);
		boolean twoPersonsInDebtWithEachOther = debtor.equals(other.debtee) && debtee.equals(other.debtor);
		boolean threePersonsWithSameDebteeAndDebtor = debtee.equals(other.debtor);
		boolean threePersonsWithSameDebteeAndDebtorReversed = debtor.equals(other.debtee);
		boolean sameTransactionWithDifferentAmount = debtor.equals(other.debtor) && debtee.equals(other.debtee);
		boolean thisTransactionIsOnlyAboutOnePerson = debtee.equals(debtor);
		boolean otherTransactionIsOnlyAboutOnePerson = other.debtee.equals(other.debtor);

		if (thisTransactionIsOnlyAboutOnePerson) {
			return transactions(other);
		}

		if (otherTransactionIsOnlyAboutOnePerson) {
			return transactions(this);
		}

		if (fourDifferentPersons || sameDebtorWithDifferentDebtees || sameDebteeWithDifferentDebtors) {
			return transactions(this, other);
		}

		if (twoPersonsInDebtWithEachOther) {

			Amount remaining = amount.minus(other.amount);

			switch (remaining.getRoundedValue().signum()) {
			case -1:
				return transactions(new Debt(debtee, remaining.absolute(), debtor));
			case 0:
				return transactions();
			case +1:
				return transactions(new Debt(debtor, remaining.absolute(), debtee));
			}
		}

		if (threePersonsWithSameDebteeAndDebtor) {

			Amount remaining = amount.minus(other.amount);

			switch (remaining.getRoundedValue().signum()) {
			case -1:
				return transactions(new Debt(debtor, amount, other.debtee),
						new Debt(debtee, remaining.absolute(), other.debtee));
			case 0:
				return transactions(new Debt(debtor, amount, other.debtee));
			case +1:
				return transactions(new Debt(debtor, other.amount, other.debtee),
						new Debt(debtor, remaining.absolute(), debtee));
			}
		}

		if (threePersonsWithSameDebteeAndDebtorReversed) {

			Amount remaining = amount.minus(other.amount);

			switch (remaining.getRoundedValue().signum()) {
			case -1:
				return transactions(new Debt(other.debtor, remaining.absolute(), other.debtee),
						new Debt(other.debtor, amount, debtee));
			case 0:
				return transactions(new Debt(other.debtor, amount, debtee));
			case +1:
				return transactions(new Debt(debtor, remaining.absolute(), debtee),
						new Debt(other.debtor, other.amount, debtee));
			}
		}

		if (sameTransactionWithDifferentAmount) {
			return transactions(new Debt(debtor, amount.plus(other.amount), other.debtee));
		}

		throw new IllegalStateException("Should not reach here");
	}

	private Set<Debt> transactions(Debt... transactions) {
		return new HashSet<>(Arrays.asList(transactions));
	}

	public Person getDebtor() {
		return debtor;
	}

	public BigDecimal getAmount() {
		return amount.getRoundedValue();
	}

	public Person getDebtee() {
		return debtee;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Debt)) return false;
		Debt that = (Debt) o;
		return Objects.equals(debtor, that.debtor) &&
				Objects.equals(amount, that.amount) &&
				Objects.equals(debtee, that.debtee);
	}

	@Override
	public int hashCode() {
		return Objects.hash(debtor, amount, debtee);
	}

	@Override
	public String toString() {
		return String.format("%s -> %s: %s", debtor, debtee, amount);
	}

}
