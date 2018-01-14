package io.nobt.rest;

import io.nobt.core.domain.NobtId;
import org.junit.Assert;
import org.junit.Test;

import java.net.URI;

import static io.nobt.test.domain.provider.ExpenseBuilderProvider.anExpense;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ExpenseLinkFactoryTest {

    @Test
    public void shouldCreateUri() {

        final RequestParameters requestParameters = mock(RequestParameters.class);
        when(requestParameters.getHost()).thenReturn("localhost:1234");
        when(requestParameters.getScheme()).thenReturn("http");

        final ExpenseLinkFactory expenseLinkFactory = new ExpenseLinkFactory(requestParameters, new NobtId("foo"));

        final URI linkToExpense = expenseLinkFactory.createLinkToExpense(anExpense().withId(1).build());

        Assert.assertEquals("http://localhost:1234/nobts/foo/expenses/1", linkToExpense.toString());
    }
}