package io.nobt.rest.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import io.nobt.core.domain.Debt;
import io.nobt.core.domain.Nobt;

import java.util.Set;

public class NobtResource {

    @JsonUnwrapped
    private Nobt nobt;

    @JsonProperty("transactions")
    private Set<Debt> transactions;

    public NobtResource(Nobt nobt, Set<Debt> transactions) {
        this.nobt = nobt;
        this.transactions = transactions;
    }
}
