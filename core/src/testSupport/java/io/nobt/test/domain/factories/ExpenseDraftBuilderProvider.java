package io.nobt.test.domain.factories;

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
                .withName(UUID.randomUUID().toString())
                .withSplitStrategy("EVENLY")
                .happendOn(LocalDate.now().minusDays(1));
    }
}
