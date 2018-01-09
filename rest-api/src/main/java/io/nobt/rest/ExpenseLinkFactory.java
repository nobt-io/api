package io.nobt.rest;

import io.nobt.core.domain.Expense;
import io.nobt.core.domain.NobtId;

import java.net.URI;

public class ExpenseLinkFactory {

    private final String scheme;
    private final String host;
    private final NobtId nobtId;

    public ExpenseLinkFactory(String scheme, String host, NobtId nobtId) {
        this.scheme = scheme;
        this.host = host;
        this.nobtId = nobtId;
    }

    public URI createLinkToExpense(Expense e) {
        return URI.create(String.format("%s://%s/nobts/%s/expenses/%d", scheme, host, nobtId.getValue(), e.getId()));
    }
}
