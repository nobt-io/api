package io.nobt.core.domain;

import io.nobt.core.optimizer.Optimizer;

import java.time.Clock;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.function.Supplier;

import static java.util.Collections.emptySet;

public final class NobtFactory {

    private final Clock clock;
    private final Supplier<Optimizer> optimizerFactory;

    public NobtFactory() {
        this(Optimizer::defaultOptimizer, Clock.systemUTC());
    }

    public NobtFactory(Supplier<Optimizer> optimizerFactory, Clock clock) {
        this.optimizerFactory = optimizerFactory;
        this.clock = clock;
    }

    public Nobt create(String name, Set<Person> explicitParticipants, CurrencyKey currencyKey) {
        return new Nobt(NobtId.newInstance(), currencyKey, name, explicitParticipants, emptySet(), emptySet(), emptySet(), ZonedDateTime.now(clock), optimizerFactory.get());
    }
}
