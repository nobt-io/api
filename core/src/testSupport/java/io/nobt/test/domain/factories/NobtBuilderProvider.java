package io.nobt.test.domain.factories;

import io.nobt.core.domain.CurrencyKey;
import io.nobt.core.domain.NobtId;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.UUID;

import static io.nobt.core.optimizer.Optimizer.defaultOptimizer;
import static io.nobt.test.domain.factories.IDProvider.nextId;
import static java.util.Collections.emptySet;

public final class NobtBuilderProvider {

    private NobtBuilderProvider() {
    }

    public static NobtBuilder aNobt() {
        return new NobtBuilder()
                .withId(new NobtId(nextId()))
                .withExpenses(emptySet())
                .withParticipants(emptySet())
                .withPayments(emptySet())
                .withCurrency(new CurrencyKey("EUR"))
                .withName(UUID.randomUUID().toString())
                .onDate(ZonedDateTime.now(ZoneOffset.UTC))
                .withOptimizer(defaultOptimizer());
    }
}
