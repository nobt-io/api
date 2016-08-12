package io.nobt.core.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static io.nobt.core.domain.Transaction.transaction;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

/**
 * An expense represents a domain entity that describes a payment of some sort done by one person ({@link Expense#debtee})
 * where several other persons take part it. (specified through the {@link Expense#shares} collection.)
 */
public class Expense {

    private final String name;
    private final Person debtee;
    private final String splitStrategy;
    private final Set<Share> shares = new HashSet<>();

    public Expense(String name, String splitStrategy, Person debtee) {
        this.name = name;
        this.splitStrategy = splitStrategy;
        this.debtee = debtee;
    }

    public String getName() {
        return name;
    }

    public String getSplitStrategy() {
        return splitStrategy;
    }

    public Amount getOverallAmount() {
        return shares.stream().map(Share::getAmount).reduce(Amount.ZERO, (Amount::plus));
    }

    public Person getDebtee() {
        return debtee;
    }

    public Set<Share> getShares() {
        return shares;
    }

    public Set<Person> getParticipants() {
        final Set<Person> debtors = shares.stream().map(Share::getDebtor).collect(toSet());

        final HashSet<Person> copyOfDebtors = new HashSet<>(debtors);
        copyOfDebtors.add(debtee);

        return copyOfDebtors;
    }

    public List<Transaction> getTransactions() {
        return shares
                .stream()
                .map(share -> transaction(share.getDebtor(), share.getAmount(), debtee))
                .collect(toList());
    }

    public Expense addShare(Share share) {
        this.shares.add(share);
        return this;
    }
}
