package io.nobt.core.domain;

import io.nobt.core.domain.transaction.Transaction;
import io.nobt.test.domain.factories.ExpenseBuilder;
import io.nobt.test.domain.factories.NobtBuilder;
import org.junit.Test;

import java.util.List;

import static io.nobt.core.domain.transaction.Transaction.transaction;
import static io.nobt.test.domain.factories.AmountFactory.amount;
import static io.nobt.test.domain.factories.ExpenseDataProvider.anEvenlySplitExpense;
import static io.nobt.test.domain.factories.ShareFactory.randomShare;
import static io.nobt.test.domain.factories.StaticPersonFactory.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class UsecaseTests {

    @Test
    public void bug_singleBillWithSingleParticipantMustNotYieldTransaction() throws Exception {

        final Nobt nobt = new NobtBuilder()
                .withExpenses(
                        new ExpenseBuilder()
                                .withShares(randomShare(thomas))
                                .withDebtee(thomas)
                                .build()
                )
                .build();

        final List<Transaction> optimalTransactions = nobt.getOptimalTransactions();
        assertThat(optimalTransactions, is(empty()));
    }

    @Test
    public void bug_invalidCalculationWithMissingTransaction() throws Exception {

        final Nobt nobt = new NobtBuilder()
                .withExpenses(
                        anEvenlySplitExpense()
                                .withDebtee(simon)
                                .withDebtors(simon, thomas, eva, matthias)
                                .withTotal(10)
                                .build(),
                        anEvenlySplitExpense()
                                .withDebtee(eva)
                                .withDebtors(simon, thomas, eva, matthias)
                                .withTotal(30)
                                .build(),
                        anEvenlySplitExpense()
                                .withDebtee(matthias)
                                .withDebtors(simon, thomas, eva, matthias)
                                .withTotal(5)
                                .build()
                )
                .build();


        final List<Transaction> optimalTransactions = nobt.getOptimalTransactions();

        assertThat(optimalTransactions, containsInAnyOrder(
                transaction(simon, amount(1.25), eva),
                transaction(thomas, amount(11.25), eva),
                transaction(matthias, amount(6.25), eva)
        ));
    }
}
