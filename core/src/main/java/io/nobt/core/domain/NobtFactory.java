package io.nobt.core.domain;

import io.nobt.core.optimizer.Optimizer;

import java.time.Clock;
import java.time.ZonedDateTime;
import java.util.Set;

import static java.util.Collections.emptySet;

public final class NobtFactory {

    private final Clock clock;

    public NobtFactory() {
        this(Clock.systemUTC());
    }

    public NobtFactory(Clock clock) {
        this.clock = clock;
    }

    public Nobt create(String name, Set<Person> explicitParticipants, CurrencyKey currencyKey) {
        return new Nobt(NobtId.newInstance(), currencyKey, name, explicitParticipants, emptySet(), ZonedDateTime.now(clock), Optimizer.defaultOptimizer());
    }
}
