package io.nobt.test.domain.factories;

import io.nobt.core.domain.CurrencyKey;
import io.nobt.core.domain.Expense;
import io.nobt.core.domain.Nobt;
import io.nobt.core.domain.Person;
import io.nobt.core.optimizer.Optimizer;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.*;

public class NobtBuilder {

    private Set<Expense> expenses = Collections.emptySet();
    private Set<Person> participants = Collections.emptySet();

    public NobtBuilder withExpenses(Expense... expenses) {
        this.expenses = new HashSet<>(Arrays.asList(expenses));
        return this;
    }

    public NobtBuilder withParticipants(Person... persons) {
        this.participants = new HashSet<>(Arrays.asList(persons));
        return this;
    }

    public Nobt build() {
        return new Nobt(
                null,
                new CurrencyKey("EUR"),
                UUID.randomUUID().toString(),
                participants,
                expenses,
                ZonedDateTime.now(ZoneOffset.UTC),
                Optimizer.defaultOptimizer()
        );
    }
}
