package io.nobt.core.domain;

import org.junit.Test;

import java.util.List;
import java.util.Set;

import static io.nobt.core.domain.Transaction.transaction;
import static io.nobt.core.domain.test.AmountFactory.amount;
import static io.nobt.core.domain.test.PersonFactory.*;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;

public class ExpenseTest {

    @Test
    public void shouldMapEveryShareToTransaction() throws Exception {

        final Expense expense = anExpense();

        final List<Transaction> transactions = expense.getTransactions();

        assertThat(transactions, containsInAnyOrder(
                transaction(david, amount(30), thomas),
                transaction(thomas, amount(10), thomas),
                transaction(lukas, amount(20), thomas)
        ));
    }

    @Test
    public void shouldReturnParticipantsOfExpense() throws Exception {
        final Expense expense = anExpense();

        final Set<Person> participants = expense.getParticipants();

        assertThat(participants, containsInAnyOrder(thomas, david, lukas));
    }

    private static Expense anExpense() {
        return new Expense("Billa", "DUMMY", thomas)
                .addShare(new Share(david, amount(30)))
                .addShare(new Share(thomas, amount(10)))
                .addShare(new Share(lukas, amount(20)));
    }
}