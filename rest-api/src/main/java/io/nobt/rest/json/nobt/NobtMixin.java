package io.nobt.rest.json.nobt;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.nobt.core.domain.*;
import io.nobt.core.domain.debt.Debt;
import io.nobt.core.optimizer.Optimizer;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public abstract class NobtMixin extends Nobt {

    public NobtMixin(NobtId id, CurrencyKey currencyKey, String name, Set<Person> explicitParticipants, Set<Expense> expenses,
                     Set<DeletedExpense> deletedExpenses, Instant createdOn, Optimizer optimizer) {
        super(id, currencyKey, name, explicitParticipants, expenses, deletedExpenses, Collections.emptySet(), createdOn, optimizer);
    }

    @Override
    @JsonProperty("debts")
    public abstract List<Debt> getOptimizedDebts();

    @JsonIgnore
    @Override
    public abstract Optimizer getOptimizer();

    @Override
    @JsonProperty("currency")
    public abstract CurrencyKey getCurrencyKey();
}
