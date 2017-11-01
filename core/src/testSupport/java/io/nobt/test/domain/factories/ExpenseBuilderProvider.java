package io.nobt.test.domain.factories;

import io.nobt.core.domain.ConversionInformation;
import io.nobt.core.domain.CurrencyKey;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static io.nobt.test.domain.factories.IDProvider.nextId;
import static java.util.Collections.emptySet;

public final class ExpenseBuilderProvider {

    private ExpenseBuilderProvider() { }

    public static ExpenseBuilder anExpense() {
        return new ExpenseBuilder()
                .withId(nextId())
                .withShares(emptySet())
                .withConversionInformation(new ConversionInformation(new CurrencyKey("EUR"), BigDecimal.ONE))
                .happendOn(LocalDate.now().minusDays(1))
                .createdOn(ZonedDateTime.now(ZoneOffset.UTC));
    }
}
