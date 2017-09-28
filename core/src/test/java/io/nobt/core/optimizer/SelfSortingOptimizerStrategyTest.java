package io.nobt.core.optimizer;

import io.nobt.core.domain.Debt;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class SelfSortingOptimizerStrategyTest {

    private SelfSortingOptimizerStrategy sut;

    @Before
    public void setUp() throws Exception {
        sut = new SelfSortingOptimizerStrategy();
    }

    @Test
    public void testShouldOptimizeTransactionList() {

        List<Debt> transactionList = Arrays.asList(
                Debt.debt("Matthias", 10, "Thomas"),
                Debt.debt("Thomas", 3, "David"),
                Debt.debt("Thomas", 10, "David"),
                Debt.debt("David", 13, "Thomas B."),
                Debt.debt("Jaci", 7, "Thomas"),
                Debt.debt("Thomas B.", 6, "Jaci"),
                Debt.debt("Jaci", 5, "Thomas B."),
                Debt.debt("Matthias", 4, "Jaci"),
                Debt.debt("Thomas", 13, "Thomas B."),
                Debt.debt("David", 17, "Thomas")
        );

        List<Debt> optimalTransactions = sut.optimize(transactionList);

        assertThat(optimalTransactions, hasSize(5));
        assertThat(optimalTransactions, containsInAnyOrder(
                Debt.debt("Matthias", 4, "Thomas"),
                Debt.debt("Jaci", 2, "Thomas B."),
                Debt.debt("Matthias", 10, "Thomas B."),
                Debt.debt("David", 4, "Thomas"),
                Debt.debt("David", 13, "Thomas B.")
                )
        );
    }

    @Test
    public void orderOfTransactionsShouldNotInfluenceResults() {

        List<Debt> transactionListA = Arrays.asList(
                Debt.debt("Matthias", 10, "Thomas"),
                Debt.debt("Thomas", 3, "David"),
                Debt.debt("Thomas", 10, "David"),
                Debt.debt("David", 13, "Thomas B."),
                Debt.debt("Jaci", 7, "Thomas"),
                Debt.debt("Thomas B.", 6, "Jaci"),
                Debt.debt("Jaci", 5, "Thomas B."),
                Debt.debt("Matthias", 4, "Jaci"),
                Debt.debt("Thomas", 13, "Thomas B."),
                Debt.debt("David", 17, "Thomas")
        );

        List<Debt> transactionListB = Arrays.asList(
                Debt.debt("Matthias", 4, "Jaci"),
                Debt.debt("Matthias", 10, "Thomas"),
                Debt.debt("Thomas", 3, "David"),
                Debt.debt("Thomas", 13, "Thomas B."),
                Debt.debt("David", 13, "Thomas B."),
                Debt.debt("Jaci", 7, "Thomas"),
                Debt.debt("Thomas B.", 6, "Jaci"),
                Debt.debt("Jaci", 5, "Thomas B."),
                Debt.debt("Thomas", 10, "David"),
                Debt.debt("David", 17, "Thomas")
        );

        List<Debt> optimalTransactionsA = sut.optimize(transactionListA);
        List<Debt> optimalTransactionsB = sut.optimize(transactionListB);

        assertThat(optimalTransactionsA, equalTo(optimalTransactionsB));
    }


}
