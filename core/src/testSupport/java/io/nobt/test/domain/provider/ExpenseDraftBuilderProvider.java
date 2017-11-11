package io.nobt.test.domain.provider;

import io.nobt.core.domain.ConversionInformation;
import io.nobt.test.domain.builder.ExpenseDraftBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static io.nobt.test.domain.factories.RandomPersonFactory.randomPerson;
import static java.util.Collections.emptyList;

public class ExpenseDraftBuilderProvider {

    private ExpenseDraftBuilderProvider() {
    }

    public static ExpenseDraftBuilder anExpenseDraft() {
        return new ExpenseDraftBuilder()
                .withShares(emptyList())
                .withDebtee(randomPerson())
                .withConversionInformation(new ConversionInformation(CurrencyKeysProvider.EUR, BigDecimal.ONE))
                .withName(UUID.randomUUID().toString())
                .withSplitStrategy("EVENLY")
                .happendOn(LocalDate.now().minusDays(1));
    }
}
