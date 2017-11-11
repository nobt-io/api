package io.nobt.core.optimizer;

import io.nobt.core.domain.debt.Debt;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static io.nobt.core.domain.debt.Debt.debt;
import static io.nobt.test.domain.factories.AmountFactory.amount;
import static io.nobt.test.domain.factories.StaticPersonFactory.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class SelfSortingMinimalAmountTransferredOptimizerStrategyTest {

    private SelfSortingMinimalAmountTransferredOptimizerStrategy sut;

    @Before
    public void setUp() throws Exception {
        sut = new SelfSortingMinimalAmountTransferredOptimizerStrategy();
    }

    @Test
    public void testShouldOptimizeDebtList() {

        List<Debt> debtList = Arrays.asList(
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

        List<Debt> optimalDebts = sut.optimize(debtList);

        assertThat(optimalDebts, allOf(
                Matchers.<Debt>iterableWithSize(6),
                containsInAnyOrder(
                        debt(matthias, amount(2), thomas),
                        debt(jacqueline, amount(1), thomas),
                        debt(jacqueline, amount(1), thomasB),
                        debt(matthias, amount(12), thomasB),
                        debt(david, amount(5), thomas),
                        debt(david, amount(12), thomasB)
                )
        ));
    }

    @Test
    public void orderOfDebtsShouldNotInfluenceResults() {

        List<Debt> debtListA = Arrays.asList(
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

        List<Debt> debtListB = Arrays.asList(
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

        List<Debt> optimalDebtsA = sut.optimize(debtListA);
        List<Debt> optimalDebtsB = sut.optimize(debtListB);

        assertThat(optimalDebtsA, equalTo(optimalDebtsB));
    }


}
