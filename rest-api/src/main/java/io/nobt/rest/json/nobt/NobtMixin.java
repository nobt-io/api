package io.nobt.rest.json.nobt;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.nobt.core.domain.CurrencyKey;
import io.nobt.core.domain.Transaction;

import java.util.List;

public abstract class NobtMixin {

    @JsonIgnore
    public abstract List<Transaction> getAllTransactions();

    @JsonProperty("currency")
    public abstract CurrencyKey getCurrencyKey();
}
