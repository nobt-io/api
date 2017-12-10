package io.nobt.core.domain.transaction.combination;

import io.nobt.core.domain.transaction.Transaction;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.nobt.core.domain.transaction.Transaction.transaction;
import static io.nobt.core.domain.transaction.combination.matchers.CombinationResultMatchers.hasChanges;
import static io.nobt.test.domain.factories.AmountFactory.amount;
import static io.nobt.test.domain.factories.StaticPersonFactory.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class RemoveTest {

    @Test
    public void shouldHaveChanges() throws Exception {
        assertThat(new Remove(), hasChanges(equalTo(true)));
    }

    @Test
    public void shouldRemoveTransactions() throws Exception {


        final Transaction transaction = transaction(thomas, amount(10), matthias);
        final Remove combinationResult = new Remove(transaction);
        final List<Transaction> list = new ArrayList<>(Arrays.asList(transaction));


        combinationResult.applyTo(list);


        assertThat(list, allOf(
                iterableWithSize(0),
                not(hasItem(transaction))
        ));

    }

    @Test
    public void shouldRemoveInstancesOnlyOnce() throws Exception {

        final Remove sut = new Remove(
                transaction(thomas, amount(2.5), simon)
        );

        final ArrayList<Transaction> existingTransactions = new ArrayList<>(
                Arrays.asList(
                        transaction(thomas, amount(2.5), simon),
                        transaction(thomas, amount(2.5), simon)
                )
        );


        sut.applyTo(existingTransactions);


        assertThat(existingTransactions, allOf(
                iterableWithSize(1),
                hasItem(
                        transaction(thomas, amount(2.5), simon)
                )
        ));
    }
}