package io.nobt.test.domain.factories;

import java.time.LocalDate;

import static io.nobt.test.domain.factories.RandomPersonFactory.randomPerson;
import static java.util.Collections.emptySet;

public class ExpenseDraftBuilderProvider {

    private ExpenseDraftBuilderProvider() {
    }

    public static ExpenseDraftBuilder anExpenseDraft() {
        return new ExpenseDraftBuilder()
                .withShares(emptySet())
                .withDebtee(randomPerson())
                .happendOn(LocalDate.now().minusDays(1));
    }
}
