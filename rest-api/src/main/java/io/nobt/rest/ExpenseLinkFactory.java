package io.nobt.rest;

import io.nobt.core.domain.Expense;
import io.nobt.core.domain.NobtId;

import java.net.URI;

public class ExpenseLinkFactory {

    private final RequestParameters requestParameters;
    private final NobtId nobtId;

    public ExpenseLinkFactory(RequestParameters requestParameters, NobtId nobtId) {
        this.requestParameters = requestParameters;
        this.nobtId = nobtId;
    }

    public URI createLinkToExpense(Expense e) {

        final String scheme = requestParameters.getScheme();
        final String host = requestParameters.getHost();

        return URI.create(String.format("%s://%s/nobts/%s/expenses/%d", scheme, host, nobtId.getValue(), e.getId()));
    }
}
