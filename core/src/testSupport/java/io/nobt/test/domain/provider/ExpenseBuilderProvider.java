package io.nobt.test.domain.provider;

import io.nobt.core.domain.ConversionInformation;
import io.nobt.test.domain.builder.ExpenseBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.UUID;

import static io.nobt.test.domain.provider.IDProvider.nextId;
import static java.util.Collections.emptySet;

public final class ExpenseBuilderProvider {

    private ExpenseBuilderProvider() {
    }

    public static ExpenseBuilder anExpense() {
        return new ExpenseBuilder()
                .withId(nextId())
                .withName(UUID.randomUUID().toString())
                .withSplitStrategy("EVENLY")
                .withShares(emptySet())
                .withConversionInformation(new ConversionInformation(CurrencyKeysProvider.EUR, BigDecimal.ONE))
                .happendOn(LocalDate.now().minusDays(1))
                .createdOn(ZonedDateTime.now(ZoneOffset.UTC));
    }
}
