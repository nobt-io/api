package io.nobt.test.domain.factories;

import io.nobt.core.domain.Expense;
import io.nobt.core.domain.Person;
import io.nobt.core.domain.Share;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ExpenseBuilder {

    private Set<Share> shares = Collections.emptySet();
    private Person debtee;

    public ExpenseBuilder withShares(Share... shares) {
        this.shares = new HashSet<Share>(Arrays.asList(shares));
        return this;
    }

    public ExpenseBuilder withDebtee(Person person) {
        debtee = person;
        return this;
    }

    public Expense build() {
        return new Expense(
                null,
                null,
                null,
                debtee,
                null,
                shares,
                LocalDate.now(),
                ZonedDateTime.now(ZoneOffset.UTC)
        );
    }
}
