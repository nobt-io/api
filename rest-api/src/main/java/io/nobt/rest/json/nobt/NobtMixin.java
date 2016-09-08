package io.nobt.rest.json.nobt;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.nobt.core.domain.Transaction;

import java.util.List;

public abstract class NobtMixin {

    @JsonIgnore
    public abstract List<Transaction> getAllTransactions();
}
