package io.nobt.core.domain.transaction.combination;

import io.nobt.core.domain.transaction.Transaction;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

public final class CompositeResult implements CombinationResult {

    private final Collection<CombinationResult> results;

    public CompositeResult(CombinationResult... results) {
        this.results = Arrays.asList(results);
    }

    @Override
    public boolean hasChanges() {
        return results.stream().reduce(false, (current, nextResult) -> current || nextResult.hasChanges(), (one, other) -> one || other);
    }

    @Override
    public void applyTo(Collection<Transaction> transactions) {
        results.forEach(result -> result.applyTo(transactions));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompositeResult that = (CompositeResult) o;
        return results.containsAll(that.results) &&
                that.results.containsAll(results);
    }

    @Override
    public int hashCode() {
        return Objects.hash(results);
    }

    @Override
    public String toString() {
        return String.format("CompositeResult{%s}", results);
    }
}
