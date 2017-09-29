package io.nobt.core.domain;

import io.nobt.core.optimizer.Optimizer;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.function.Supplier;

import static java.util.Collections.emptySet;

public final class NobtFactory {

    private final Supplier<Optimizer> optimizerFactory;
    private final Supplier<ZonedDateTime> nowFactory;

    public NobtFactory() {
        this(Optimizer::defaultOptimizer, () -> ZonedDateTime.now(ZoneOffset.UTC));
    }

    public NobtFactory(Supplier<Optimizer> optimizerFactory, Supplier<ZonedDateTime> nowFactory) {
        this.optimizerFactory = optimizerFactory;
        this.nowFactory = nowFactory;
    }

    public Nobt create(String name, Set<Person> explicitParticipants, CurrencyKey currencyKey) {
        return new Nobt(null, currencyKey, name, explicitParticipants, emptySet(), nowFactory.get(), optimizerFactory.get());
    }
}
