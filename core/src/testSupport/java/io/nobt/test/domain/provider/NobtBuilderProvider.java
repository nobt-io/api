package io.nobt.test.domain.provider;

import io.nobt.core.domain.CurrencyKey;
import io.nobt.core.domain.NobtId;
import io.nobt.test.domain.builder.NobtBuilder;

import java.time.Clock;
import java.util.UUID;

import static io.nobt.core.optimizer.Optimizer.defaultOptimizer;
import static java.util.Collections.emptySet;

public final class NobtBuilderProvider {

    private NobtBuilderProvider() {
    }

    public static NobtBuilder aNobt() {
        return new NobtBuilder()
                .withId(NobtId.newInstance())
                .withExpenses(emptySet())
                .withDeletedExpenses(emptySet())
                .withParticipants(emptySet())
                .withPayments(emptySet())
                .withCurrency(new CurrencyKey("EUR"))
                .withName(UUID.randomUUID().toString())
                .onDate(Clock.systemUTC().instant())
                .withOptimizer(defaultOptimizer());
    }
}
