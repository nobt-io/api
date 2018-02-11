package io.nobt.rest.links;

import io.nobt.core.domain.Expense;
import io.nobt.core.domain.Nobt;

import java.net.URI;

public class ExpenseLinkFactory implements LinkFactory<Expense> {

    private final BasePath basePath;
    private final Nobt nobt;

    public ExpenseLinkFactory(BasePath basePath, Nobt nobt) {
        this.basePath = basePath;
        this.nobt = nobt;
    }

    @Override
    public URI createLinkTo(Expense expense) {

        final String nobtId = nobt.getId().getValue();
        final long expenseId = expense.getId();

        return URI.create(String.format("%s/nobts/%s/expenses/%d", basePath.asString(), nobtId, expenseId));
    }
}
