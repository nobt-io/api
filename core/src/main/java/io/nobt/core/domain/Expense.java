package io.nobt.core.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
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

    private final Long id;
    private final String name;
    private final Person debtee;
    private final String splitStrategy;
    private final Set<Share> shares;
    private final LocalDate date;
    private final LocalDateTime createdOn;

    public Expense(Long id, String name, String splitStrategy, Person debtee, Set<Share> shares, LocalDate date, LocalDateTime createdOn) {
        this.id = id;
        this.name = name;
        this.splitStrategy = splitStrategy;
        this.debtee = debtee;
        this.shares = shares;
        this.date = date;
        this.createdOn = createdOn;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSplitStrategy() {
        return splitStrategy;
    }

    public Person getDebtee() {
        return debtee;
    }

    public Set<Share> getShares() {
        return Collections.unmodifiableSet(shares);
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
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
}
