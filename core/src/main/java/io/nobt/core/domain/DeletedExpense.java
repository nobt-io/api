package io.nobt.core.domain;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Set;

public class DeletedExpense {

    private final Expense originalExpense;
    private final Instant deletedOn;

    public DeletedExpense(Expense originalExpense) {
        this(originalExpense, Clock.systemUTC().instant());
    }

    public DeletedExpense(Expense originalExpense, Instant deletedOn) {
        this.originalExpense = originalExpense;
        this.deletedOn = deletedOn;
    }

    public long getId() {
        return originalExpense.getId();
    }

    public String getName() {
        return originalExpense.getName();
    }

    public String getSplitStrategy() {
        return originalExpense.getSplitStrategy();
    }

    public Person getDebtee() {
        return originalExpense.getDebtee();
    }

    public Set<Share> getShares() {
        return originalExpense.getShares();
    }

    public LocalDate getDate() {
        return originalExpense.getDate();
    }

    public Instant getCreatedOn() {
        return originalExpense.getCreatedOn();
    }

    public ConversionInformation getConversionInformation() {
        return originalExpense.getConversionInformation();
    }

    public Set<Person> getParticipants() {
        return originalExpense.getParticipants();
    }

    public Expense getOriginalExpense() {
        return originalExpense;
    }

    public Instant getDeletedOn() {
        return deletedOn;
    }
}
