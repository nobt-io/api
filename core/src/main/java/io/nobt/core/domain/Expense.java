package io.nobt.core.domain;

import io.nobt.core.domain.transaction.Debt;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

/**
 * An expense represents a domain entity that describes a payment of some sort done by one person ({@link Expense#debtee})
 * where several other persons take part it. (specified through the {@link Expense#shares} collection.)
 */
public class Expense implements CashFlow {

    private final Long id;
    private final String name;
    private final Person debtee;
    private final String splitStrategy;
    private final ConversionInformation conversionInformation;
    private final Set<Share> shares;
    private final LocalDate date;
    private final ZonedDateTime createdOn;

    public Expense(Long id, String name, String splitStrategy, Person debtee, ConversionInformation conversionInformation, Set<Share> shares, LocalDate date, ZonedDateTime createdOn) {
        this.id = id;
        this.name = name;
        this.splitStrategy = splitStrategy;
        this.debtee = debtee;
        this.conversionInformation = conversionInformation;
        this.shares = shares;
        this.date = date;
        this.createdOn = createdOn;
    }

    public long getId() {
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

    @Override
    public Set<Debt> calculateAccruingDebts() {
        return shares
                .stream()
                .map(share -> Debt.debt(share.getDebtor(), share.getAmount(), debtee))
                .collect(toSet());
    }

    public ZonedDateTime getCreatedOn() {
        return createdOn;
    }

    public ConversionInformation getConversionInformation() {
        return conversionInformation;
    }

    public Set<Person> getParticipants() {
        final Set<Person> debtors = shares.stream().map(Share::getDebtor).collect(toSet());

        final HashSet<Person> copyOfDebtors = new HashSet<>(debtors);
        copyOfDebtors.add(debtee);

        return copyOfDebtors;
    }
}
