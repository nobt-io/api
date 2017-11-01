package io.nobt.core.domain.debt.combination;

import io.nobt.core.domain.debt.Debt;
import io.nobt.test.domain.factories.AmountFactory;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.nobt.core.domain.debt.Debt.debt;
import static io.nobt.core.domain.debt.combination.matchers.CombinationResultMatchers.hasChanges;
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


        final Debt debt = debt(thomas, AmountFactory.amount(10), matthias);
        final Remove combinationResult = new Remove(debt);
        final List<Debt> list = new ArrayList<>(Arrays.asList(debt));


        combinationResult.applyTo(list);


        assertThat(list, allOf(
                iterableWithSize(0),
                not(hasItem(debt))
        ));

    }
}