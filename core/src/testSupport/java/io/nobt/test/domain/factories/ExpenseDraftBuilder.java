package io.nobt.test.domain.factories;

import io.nobt.core.domain.ConversionInformation;
import io.nobt.core.domain.ExpenseDraft;
import io.nobt.core.domain.Person;
import io.nobt.core.domain.Share;

import java.time.LocalDate;
import java.util.Set;

public class ExpenseDraftBuilder {

    private String name;
    private String splitStrategy;
    private Person debtee;
    private Set<Share> shares;
    private LocalDate date;
    private ConversionInformation conversionInformation;

    public ExpenseDraftBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public ExpenseDraftBuilder withSplitStrategy(String splitStrategy) {
        this.splitStrategy = splitStrategy;
        return this;
    }

    public ExpenseDraftBuilder withDebtee(Person debtee) {
        this.debtee = debtee;
        return this;
    }

    public ExpenseDraftBuilder withShares(Set<Share> shares) {
        this.shares = shares;
        return this;
    }

    public ExpenseDraftBuilder happendOn(LocalDate date) {
        this.date = date;
        return this;
    }

    public ExpenseDraftBuilder withConversionInformation(ConversionInformation conversionInformation) {
        this.conversionInformation = conversionInformation;
        return this;
    }

    public ExpenseDraft build() {
        return new ExpenseDraft(name, splitStrategy, debtee, shares, date, conversionInformation);
    }
}