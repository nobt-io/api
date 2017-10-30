package io.nobt.core.domain.transaction.combination;

import io.nobt.core.domain.transaction.Transaction;
import io.nobt.test.domain.factories.AmountFactory;
import org.junit.Test;

import java.util.ArrayList;

import static io.nobt.core.domain.transaction.combination.matchers.CombinationResultMatchers.hasChanges;
import static io.nobt.test.domain.factories.StaticPersonFactory.matthias;
import static io.nobt.test.domain.factories.StaticPersonFactory.thomas;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class AddTest {

    @Test
    public void shouldHaveChanges() throws Exception {
        assertThat(new Add(), hasChanges(equalTo(true)));
    }

    @Test
    public void shouldAddItemsToList() throws Exception {

        final Transaction transaction = Transaction.transaction(thomas, AmountFactory.amount(10), matthias);
        final Add combinationResult = new Add(transaction);
        final ArrayList<Transaction> list = new ArrayList<>();


        combinationResult.applyTo(list);


        assertThat(list, allOf(
                iterableWithSize(1),
                hasItem(transaction)
        ));
    }
}