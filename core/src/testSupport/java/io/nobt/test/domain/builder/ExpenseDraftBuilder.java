package io.nobt.test.domain.builder;

import io.nobt.core.domain.ConversionInformation;
import io.nobt.core.domain.ExpenseDraft;
import io.nobt.core.domain.Person;
import io.nobt.core.domain.Share;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class ExpenseDraftBuilder {

    private String name;
    private String splitStrategy;
    private Person debtee;
    private List<Share> shares;
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

    public ExpenseDraftBuilder withShares(Share... shares) {
        return withShares(Arrays.stream(shares).collect(toList()));
    }

    public ExpenseDraftBuilder withShares(List<Share> shares) {
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