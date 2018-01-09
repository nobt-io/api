package io.nobt.rest;

import io.nobt.core.domain.NobtId;
import org.junit.Assert;
import org.junit.Test;

import java.net.URI;

import static io.nobt.test.domain.provider.ExpenseBuilderProvider.anExpense;

public class ExpenseLinkFactoryTest {

    @Test
    public void shouldCreateUri() {

        final ExpenseLinkFactory expenseLinkFactory = new ExpenseLinkFactory("http", "localhost:1234", new NobtId("foo"));

        final URI linkToExpense = expenseLinkFactory.createLinkToExpense(anExpense().withId(1).build());

        Assert.assertEquals("http://localhost:1234/nobts/foo/expenses/1", linkToExpense.toString());
    }
}