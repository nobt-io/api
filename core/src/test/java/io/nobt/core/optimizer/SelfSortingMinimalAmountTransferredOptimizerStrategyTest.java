package io.nobt.core.optimizer;

import io.nobt.core.domain.transaction.Debt;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static io.nobt.core.domain.transaction.Debt.debt;
import static io.nobt.test.domain.factories.AmountFactory.amount;
import static io.nobt.test.domain.factories.StaticPersonFactory.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class SelfSortingMinimalAmountTransferredOptimizerStrategyTest {
    private static final Logger LOGGER = LogManager.getLogger();

    private SelfSortingMinimalAmountTransferredOptimizerStrategy sut;

    @Before
    public void setUp() throws Exception {
        sut = new SelfSortingMinimalAmountTransferredOptimizerStrategy();
    }

    @Test
    public void testShouldOptimizeTransactionList() {

        List<Debt> transactionList = Arrays.asList(
                debt(matthias, amount(10), thomas),
                debt(thomas, amount(3), david),
                debt(thomas, amount(10), david),
                debt(david, amount(13), thomasB),
                debt(jacqueline, amount(7), thomas),
                debt(thomasB, amount(6), jacqueline),
                debt(jacqueline, amount(5), thomasB),
                debt(matthias, amount(4), jacqueline),
                debt(thomas, amount(13), thomasB),
                debt(david, amount(17), thomas)
        );

        List<Debt> optimalTransactions = sut.optimize(transactionList);

        assertThat(optimalTransactions, allOf(
                Matchers.<Debt>iterableWithSize(5),
                containsInAnyOrder(
                        debt(matthias, amount(4), thomas),
                        debt(jacqueline, amount(1), thomasB),
                        debt(matthias, amount(10), thomasB),
                        debt(david, amount(6), thomas),
                        debt(david, amount(11), thomasB)
                )
        ));
    }

    @Test
    public void orderOfTransactionsShouldNotInfluenceResults() {

        List<Debt> transactionListA = Arrays.asList(
                debt(matthias, amount(10), thomas),
                debt(thomas, amount(3), david),
                debt(thomas, amount(10), david),
                debt(david, amount(13), thomasB),
                debt(jacqueline, amount(7), thomas),
                debt(thomasB, amount(6), jacqueline),
                debt(jacqueline, amount(5), thomasB),
                debt(matthias, amount(4), jacqueline),
                debt(thomas, amount(13), thomasB),
                debt(david, amount(17), thomas)
        );

        List<Debt> transactionListB = Arrays.asList(
                debt(david, amount(17), thomas),
                debt(david, amount(13), thomasB),
                debt(matthias, amount(4), jacqueline),
                debt(thomas, amount(13), thomasB),
                debt(matthias, amount(10), thomas),
                debt(thomas, amount(10), david),
                debt(thomas, amount(3), david),
                debt(thomasB, amount(6), jacqueline),
                debt(jacqueline, amount(7), thomas),
                debt(jacqueline, amount(5), thomasB)
        );

        List<Debt> optimalTransactionsA = sut.optimize(transactionListA);
        List<Debt> optimalTransactionsB = sut.optimize(transactionListB);

        assertThat(optimalTransactionsA, equalTo(optimalTransactionsB));
    }


}
