package io.nobt.rest.links;

import io.nobt.core.domain.Expense;
import io.nobt.core.domain.Nobt;

import java.net.URI;

public class ExpenseLinkFactory {

    private final BasePath basePath;
    private final Nobt nobt;

    public ExpenseLinkFactory(BasePath basePath, Nobt nobt) {
        this.basePath = basePath;
        this.nobt = nobt;
    }

    public URI createLinkToExpense(Expense e) {

        final String nobtId = nobt.getId().getValue();
        final long expenseId = e.getId();

        return URI.create(String.format("%s/nobts/%s/expenses/%d", basePath.asString(), nobtId, expenseId));
    }
}
