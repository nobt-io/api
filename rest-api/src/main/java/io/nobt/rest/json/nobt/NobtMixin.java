package io.nobt.rest.json.nobt;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.nobt.core.domain.*;
import io.nobt.core.optimizer.OptimizerVersion;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;

public abstract class NobtMixin extends Nobt {

    public NobtMixin(NobtId id, CurrencyKey currencyKey, String name, Set<Person> explicitParticipants, Set<Expense> expenses, ZonedDateTime createdOn, OptimizerVersion optimizerVersion) {
        super(id, currencyKey, name, explicitParticipants, expenses, createdOn, optimizerVersion);
    }

    @Override
    @JsonProperty("transactions")
    public abstract List<Transaction> getOptimalTransactions();

    @JsonIgnore
	@Override
	public abstract OptimizerVersion getOptimizerVersion();

    @Override
	@JsonProperty("currency")
    public abstract CurrencyKey getCurrencyKey();
}
