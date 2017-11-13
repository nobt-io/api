package io.nobt.core.domain.transaction.combination;

import io.nobt.core.domain.transaction.Transaction;
import io.nobt.test.domain.factories.AmountFactory;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.nobt.core.domain.transaction.combination.matchers.CombinationResultMatchers.hasChanges;
import static io.nobt.test.domain.factories.StaticPersonFactory.matthias;
import static io.nobt.test.domain.factories.StaticPersonFactory.thomas;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class RemoveTest {

    @Test
    public void shouldHaveChanges() throws Exception {
        assertThat(new Remove(), hasChanges(equalTo(true)));
    }

    @Test
    public void shouldRemoveTransactions() throws Exception {


        final Transaction transaction = Transaction.transaction(thomas, AmountFactory.amount(10), matthias);
        final Remove combinationResult = new Remove(transaction);
        final List<Transaction> list = new ArrayList<>(Arrays.asList(transaction));


        combinationResult.applyTo(list);


        assertThat(list, allOf(
                iterableWithSize(0),
                not(hasItem(transaction))
        ));

    }
}