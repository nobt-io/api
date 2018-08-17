package io.nobt.test.domain.provider;

import io.nobt.core.domain.ConversionInformation;
import io.nobt.test.domain.builder.EvenlySplitExpenseBuilder;
import io.nobt.test.domain.builder.ExpenseBuilder;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import static io.nobt.test.domain.factories.StaticPersonFactory.*;
import static io.nobt.test.domain.provider.IDProvider.nextId;
import static java.util.Collections.emptySet;

public final class ExpenseBuilderProvider {

    private ExpenseBuilderProvider() {
    }

    public static ExpenseBuilder anExpense() {
        return new ExpenseBuilder()
                .withId(nextId())
                .withName(UUID.randomUUID().toString())
                .withDebtee(thomas)
                .withSplitStrategy("EVENLY")
                .withShares(emptySet())
                .withConversionInformation(new ConversionInformation(CurrencyKeysProvider.EUR, BigDecimal.ONE))
                .happendOn(LocalDate.now().minusDays(1))
                .createdOn(Instant.now(Clock.systemUTC()));
    }

    public static EvenlySplitExpenseBuilder anEvenlySplitExpense() {
        return new EvenlySplitExpenseBuilder(anExpense());
    }
}
