package io.nobt.core.domain;

import io.nobt.core.domain.debt.Debt;
import org.junit.Test;

import java.util.Set;

import static io.nobt.core.domain.debt.Debt.debt;
import static io.nobt.test.domain.factories.AmountFactory.amount;
import static io.nobt.test.domain.factories.ExpenseBuilderProvider.anExpense;
import static io.nobt.test.domain.factories.ShareFactory.randomShare;
import static io.nobt.test.domain.factories.StaticPersonFactory.*;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;

public class ExpenseTest {

    @Test
    public void shouldMapEveryShareToADebt() throws Exception {

        final Expense expense = anExpense()
                .withDebtee(thomas)
                .withShares(
                        new Share(david, amount(30)),
                        new Share(thomas, amount(10)),
                        new Share(lukas, amount(20))
                )
                .build();


        final Set<Debt> debts = expense.calculateAccruingDebts();


        assertThat(debts, containsInAnyOrder(
                debt(david, amount(30), thomas),
                debt(thomas, amount(10), thomas),
                debt(lukas, amount(20), thomas)
        ));
    }

    @Test
    public void shouldReturnParticipantsOfExpense() throws Exception {

        final Expense expense = anExpense()
                .withDebtee(thomas)
                .withShares(
                        randomShare(thomas),
                        randomShare(david),
                        randomShare(lukas)
                )
                .build();


        final Set<Person> participants = expense.getParticipants();


        assertThat(participants, containsInAnyOrder(thomas, david, lukas));
    }
}