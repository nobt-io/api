package io.nobt.rest.links;

import io.nobt.core.domain.Expense;
import io.nobt.core.domain.Nobt;
import io.nobt.core.domain.NobtId;
import org.junit.Assert;
import org.junit.Test;

import java.net.URI;

import static io.nobt.test.domain.provider.ExpenseBuilderProvider.anExpense;
import static io.nobt.test.domain.provider.NobtBuilderProvider.aNobt;

public class ExpenseLinkFactoryTest {

    @Test
    public void shouldCreateUri() {

        final Nobt nobt = aNobt().withId(new NobtId("foo")).build();
        final LinkFactory<Expense> expenseLinkFactory = new ExpenseLinkFactory(new BasePath("http", "localhost:1234"), nobt);

        final URI linkToExpense = expenseLinkFactory.createLinkTo(anExpense().withId(1).build());

        Assert.assertEquals("http://localhost:1234/nobts/foo/expenses/1", linkToExpense.toString());
    }
}