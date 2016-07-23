package io.nobt.rest.payloads;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

import io.nobt.core.domain.Nobt;
import io.nobt.core.domain.Transaction;

public class NobtResource {

    @JsonUnwrapped
    private Nobt nobt;

    @JsonProperty("transactions")
    private Set<Transaction> transactions;

    public NobtResource(Nobt nobt, Set<Transaction> transactions) {
        this.nobt = nobt;
        this.transactions = transactions;
    }
}
