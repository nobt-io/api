package io.nobt.core.domain;

import io.nobt.core.optimizer.Optimizer;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Set;

import static java.util.Collections.emptySet;

public final class NobtFactory {

    public Nobt create(String name, Set<Person> explicitParticipants, CurrencyKey currencyKey) {
        return new Nobt(null, currencyKey, name, explicitParticipants, emptySet(), ZonedDateTime.now(ZoneOffset.UTC), Optimizer.defaultOptimizer());
    }
}
