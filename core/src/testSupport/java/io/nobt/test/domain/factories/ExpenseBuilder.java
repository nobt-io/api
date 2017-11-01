package io.nobt.test.domain.factories;

import io.nobt.core.domain.ConversionInformation;
import io.nobt.core.domain.Expense;
import io.nobt.core.domain.Person;
import io.nobt.core.domain.Share;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public class ExpenseBuilder {

    private Set<Share> shares;
    private Person debtee;
    private LocalDate date;
    private long id;
    private String name;
    private String splitStrategy;
    private ConversionInformation conversionInformation;
    private ZonedDateTime createdOn;

    public ExpenseBuilder withShares(Share... shares) {
        return withShares(Arrays.stream(shares).collect(toSet()));
    }

    public ExpenseBuilder withShares(Set<Share> shares) {
        this.shares = shares;
        return this;
    }

    public ExpenseBuilder withDebtee(Person person) {
        debtee = person;
        return this;
    }

    public ExpenseBuilder happendOn(LocalDate date) {
        this.date = date;
        return this;
    }

    public ExpenseBuilder withId(long id) {
        this.id = id;
        return this;
    }

    public ExpenseBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public ExpenseBuilder withSplitStrategy(String splitStrategy) {
        this.splitStrategy = splitStrategy;
        return this;
    }

    public ExpenseBuilder withConversionInformation(ConversionInformation conversionInformation) {
        this.conversionInformation = conversionInformation;
        return this;
    }

    public ExpenseBuilder createdOn(ZonedDateTime dateTime) {
        this.createdOn = dateTime;
        return this;
    }

    public Expense build() {
        return new Expense(
                id,
                name,
                splitStrategy,
                debtee,
                conversionInformation,
                shares,
                date,
                createdOn
        );
    }
}
