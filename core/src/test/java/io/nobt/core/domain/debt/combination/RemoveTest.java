package io.nobt.core.domain.debt.combination;

import io.nobt.core.domain.debt.Debt;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.nobt.core.domain.debt.Debt.debt;
import static io.nobt.test.domain.factories.AmountFactory.amount;
import static io.nobt.test.domain.factories.StaticPersonFactory.*;
import static io.nobt.test.domain.matchers.CombinationResultMatchers.hasChanges;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class RemoveTest {

    @Test
    public void shouldHaveChanges() throws Exception {
        assertThat(new Remove(), hasChanges(equalTo(true)));
    }

    @Test
    public void shouldRemoveDebts() throws Exception {


        final Debt debt = debt(thomas, amount(10), matthias);
        final Remove combinationResult = new Remove(debt);
        final List<Debt> list = new ArrayList<>(Arrays.asList(debt));


        combinationResult.applyTo(list);


        assertThat(list, allOf(
                iterableWithSize(0),
                not(hasItem(debt))
        ));

    }

    @Test
    public void shouldRemoveInstancesOnlyOnce() throws Exception {

        final Remove sut = new Remove(
                debt(thomas, amount(2.5), simon)
        );

        final ArrayList<Debt> existingTransactions = new ArrayList<>(
                Arrays.asList(
                        debt(thomas, amount(2.5), simon),
                        debt(thomas, amount(2.5), simon)
                )
        );


        sut.applyTo(existingTransactions);


        assertThat(existingTransactions, allOf(
                iterableWithSize(1),
                hasItem(
                        debt(thomas, amount(2.5), simon)
                )
        ));
    }
}