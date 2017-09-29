package io.nobt.core.domain;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Debt {

    private final Amount amount;

    /**
     * Owes money.
     */
    private final Person debtor;

    /**
     * Receives money.
     */
    private final Person debtee;

    private Debt(Person debtor, Amount amount, Person debtee) {
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

    public Set<Debt> combine(Debt other) {

        HashSet<Person> participatingPersons = new HashSet<>(Arrays.asList(debtor, debtee, other.debtor, other.debtee));

        boolean fourDifferentPersons = participatingPersons.size() == 4;

        boolean sameDebtorWithDifferentDebtees = debtor.equals(other.debtor) && !debtee.equals(other.debtee);
        boolean sameDebteeWithDifferentDebtors = debtee.equals(other.debtee) && !debtor.equals(other.debtor);

        boolean twoPersonsInDebtWithEachOther = debtor.equals(other.debtee) && debtee.equals(other.debtor);

        boolean threePersonsWithSameDebteeAndDebtor = debtee.equals(other.debtor);
        boolean threePersonsWithSameDebteeAndDebtorReversed = debtor.equals(other.debtee);

        boolean sameDebtWithDifferentAmount = debtor.equals(other.debtor) && debtee.equals(other.debtee);

        boolean thisDebtIsOnlyAboutOnePerson = debtee.equals(debtor);
        boolean otherDebtIsOnlyAboutOnePerson = other.debtee.equals(other.debtor);

        if (thisDebtIsOnlyAboutOnePerson) {
            return debts(other);
        }

        if (otherDebtIsOnlyAboutOnePerson) {
            return debts(this);
        }

        if (fourDifferentPersons || sameDebtorWithDifferentDebtees || sameDebteeWithDifferentDebtors) {
            return debts(this, other);
        }

        if (twoPersonsInDebtWithEachOther) {

            Amount remaining = amount.minus(other.amount);

            switch (remaining.getRoundedValue().signum()) {
                case -1:
                    return debts(
                            new Debt(debtee, remaining.absolute(), debtor)
                    );
                case 0:
                    return debts();
                case +1:
                    return debts(
                            new Debt(debtor, remaining.absolute(), debtee)
                    );
            }
        }

        if (threePersonsWithSameDebteeAndDebtor) {

            Amount remaining = amount.minus(other.amount);

            switch (remaining.getRoundedValue().signum()) {
                case -1:
                    return debts(
                            new Debt(debtor, amount, other.debtee),
                            new Debt(debtee, remaining.absolute(), other.debtee)
                    );
                case 0:
                    return debts(
                            new Debt(debtor, amount, other.debtee)
                    );
                case +1:
                    return debts(
                            new Debt(debtor, other.amount, other.debtee),
                            new Debt(debtor, remaining.absolute(), debtee)
                    );
            }
        }

        if (threePersonsWithSameDebteeAndDebtorReversed) {

            Amount remaining = amount.minus(other.amount);

            switch (remaining.getRoundedValue().signum()) {
                case -1:
                    return debts(
                            new Debt(other.debtor, remaining.absolute(), other.debtee),
                            new Debt(other.debtor, amount, debtee)
                    );
                case 0:
                    return debts(
                            new Debt(other.debtor, amount, debtee)
                    );
                case +1:
                    return debts(
                            new Debt(debtor, remaining.absolute(), debtee),
                            new Debt(other.debtor, other.amount, debtee)
                    );
            }
        }

        if (sameDebtWithDifferentAmount) {
            return debts(
                    new Debt(debtor, amount.plus(other.amount), other.debtee)
            );
        }

        throw new IllegalStateException("Should not reach here");
    }

    private Set<Debt> debts(Debt... debts) {
        return new HashSet<>(Arrays.asList(debts));
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
