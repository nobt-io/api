package io.nobt.core.domain.debt;

import io.nobt.core.domain.Amount;
import io.nobt.core.domain.Person;
import io.nobt.core.domain.debt.combination.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Debt {

    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * Gives money.
     */
    private Person debtor;
    private Amount amount;

    /**
     * Receives money.
     */
    private Person debtee;

    protected Debt(Person debtor, Amount amount, Person debtee) {
        this.debtor = debtor;
        this.amount = amount;
        this.debtee = debtee;
    }

    public static Debt debt(Person debtor, Amount amount, Person debtee) {
        if (!amount.isPositive()) {
            throw new IllegalArgumentException("Amount must be positive.");
        }
        return new Debt(debtor, amount, debtee);
    }

    public CombinationResult combine(Debt other) {

        final Set<Person> participatingPersons = new HashSet<>(Arrays.asList(debtor, debtee, other.debtor, other.debtee));

        boolean fourDifferentPersons = participatingPersons.size() == 4;
        boolean sameDebtorWithDifferentDebtees = debtor.equals(other.debtor) && !debtee.equals(other.debtee);
        boolean sameDebteeWithDifferentDebtors = debtee.equals(other.debtee) && !debtor.equals(other.debtor);
        boolean twoPersonsInDebtWithEachOther = debtor.equals(other.debtee) && debtee.equals(other.debtor);
        boolean threePersonsWithSameDebteeAndDebtor = debtee.equals(other.debtor);
        boolean threePersonsWithSameDebteeAndDebtorReversed = debtor.equals(other.debtee);
        boolean sameDebtWithDifferentAmount = debtor.equals(other.debtor) && debtee.equals(other.debtee);
        boolean thisDebtIsOnlyAboutOnePerson = debtee.equals(debtor);
        boolean otherDebtIsOnlyAboutOnePerson = other.debtee.equals(other.debtor);
        boolean combiningWithItself = this == other;

        if (combiningWithItself && participatingPersons.size() == 1) {
            return new Remove(this);
        }

        if (combiningWithItself && participatingPersons.size() == 2) {
            return new NotCombinable();
        }

        if (thisDebtIsOnlyAboutOnePerson) {
            return new Remove(this);
        }

        if (otherDebtIsOnlyAboutOnePerson) {
            return new Remove(other);
        }

        if (fourDifferentPersons || sameDebtorWithDifferentDebtees || sameDebteeWithDifferentDebtors) {
            return new NotCombinable();
        }

        if (twoPersonsInDebtWithEachOther) {

            Amount remaining = amount.minus(other.amount);

            switch (remaining.getRoundedValue().signum()) {
                case -1:
                    return new CompositeResult(
                            new Remove(this, other),
                            new Add(new Debt(debtee, remaining.absolute(), debtor))
                    );
                case 0:
                    return new Remove(this, other);
                case +1:
                    return new CompositeResult(
                            new Remove(this, other),
                            new Add(new Debt(debtor, remaining.absolute(), debtee))
                    );
            }
        }

        if (threePersonsWithSameDebteeAndDebtor) {

            Amount remaining = amount.minus(other.amount);

            switch (remaining.getRoundedValue().signum()) {
                case -1:
                    return new CompositeResult(
                            new Remove(this, other),
                            new Add(
                                    new Debt(debtor, amount, other.debtee),
                                    new Debt(debtee, remaining.absolute(), other.debtee)
                            )
                    );
                case 0:
                    return new CompositeResult(
                            new Remove(this, other),
                            new Add(new Debt(debtor, amount, other.debtee))
                    );
                case +1:
                    return new CompositeResult(
                            new Remove(this, other),
                            new Add(
                                    new Debt(debtor, other.amount, other.debtee),
                                    new Debt(debtor, remaining.absolute(), debtee)
                            )
                    );
            }
        }

        if (threePersonsWithSameDebteeAndDebtorReversed) {

            Amount remaining = amount.minus(other.amount);

            switch (remaining.getRoundedValue().signum()) {
                case -1:
                    return new CompositeResult(
                            new Remove(this, other),
                            new Add(
                                    new Debt(other.debtor, remaining.absolute(), other.debtee),
                                    new Debt(other.debtor, amount, debtee))
                    );
                case 0:
                    return new CompositeResult(
                            new Remove(this, other),
                            new Add(new Debt(other.debtor, amount, debtee))
                    );
                case +1:
                    return new CompositeResult(
                            new Remove(this, other),
                            new Add(
                                    new Debt(debtor, remaining.absolute(), debtee),
                                    new Debt(other.debtor, other.amount, debtee)
                            )
                    );
            }
        }

        if (sameDebtWithDifferentAmount) {
            return new CompositeResult(
                    new Remove(this, other),
                    new Add(new Debt(debtor, amount.plus(other.amount), other.debtee))
            );
        }

        return new IllegalCombination();
    }

    public Person getDebtor() {
        return debtor;
    }

    public BigDecimal getRoundedAmount() {
        return amount.getRoundedValue();
    }

    public Amount getAmount() {
        return amount;
    }

    public Person getDebtee() {
        return debtee;
    }

    public Debt withNewAmount(Amount newAmount) {
        return new Debt(this.debtor, newAmount, this.debtee);
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
