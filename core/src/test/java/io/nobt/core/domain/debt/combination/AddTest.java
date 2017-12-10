package io.nobt.core.domain.debt.combination;

import io.nobt.core.domain.debt.Debt;
import io.nobt.test.domain.factories.AmountFactory;
import org.junit.Test;

import java.util.ArrayList;

import static io.nobt.test.domain.factories.StaticPersonFactory.matthias;
import static io.nobt.test.domain.factories.StaticPersonFactory.thomas;
import static io.nobt.test.domain.matchers.CombinationResultMatchers.hasChanges;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class AddTest {

    @Test
    public void shouldHaveChanges() throws Exception {
        assertThat(new Add(), hasChanges(equalTo(true)));
    }

    @Test
    public void shouldAddItemsToList() throws Exception {

        final Debt debt = Debt.debt(thomas, AmountFactory.amount(10), matthias);
        final Add combinationResult = new Add(debt);
        final ArrayList<Debt> list = new ArrayList<>();


        combinationResult.applyTo(list);


        assertThat(list, allOf(
                iterableWithSize(1),
                hasItem(debt)
        ));
    }
}