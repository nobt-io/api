package io.nobt.core;

import io.nobt.core.domain.Transaction;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static io.nobt.core.domain.Transaction.transaction;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class TransactionListOptimizerTest {

    @Test
    public void testShouldOptimizeTransactionList() {

        List<Transaction> transactionList = Arrays.asList(
                transaction("Matthias", 10, "Thomas"),
                transaction("Thomas", 3, "David"),
                transaction("Thomas", 10, "David"),
                transaction("David", 13, "Thomas B."),
                transaction("Jaci", 7, "Thomas"),
                transaction("Thomas B.", 6, "Jaci"),
                transaction("Jaci", 5, "Thomas B."),
                transaction("Matthias", 4, "Jaci"),
                transaction("Thomas", 13, "Thomas B."),
                transaction("David", 17, "Thomas")
        );

        TransactionListOptimizer optimizer = new TransactionListOptimizer(transactionList);

        List<Transaction> optimalTransactions = optimizer.getOptimalTransactions();

        assertThat(optimalTransactions, hasSize(5));
        assertThat(optimalTransactions, containsInAnyOrder(
                transaction("Matthias", 4, "Thomas"),
                transaction("Jaci", 2, "Thomas B."),
                transaction("Matthias", 10, "Thomas B."),
                transaction("David", 4, "Thomas"),
                transaction("David", 13, "Thomas B.")
                )
        );
    }

    @Test
    public void orderOfTransactionsShouldNotInfluenceResults() {

        List<Transaction> transactionListA = Arrays.asList(
                transaction("Matthias", 10, "Thomas"),
                transaction("Thomas", 3, "David"),
                transaction("Thomas", 10, "David"),
                transaction("David", 13, "Thomas B."),
                transaction("Jaci", 7, "Thomas"),
                transaction("Thomas B.", 6, "Jaci"),
                transaction("Jaci", 5, "Thomas B."),
                transaction("Matthias", 4, "Jaci"),
                transaction("Thomas", 13, "Thomas B."),
                transaction("David", 17, "Thomas")
        );

        List<Transaction> transactionListB = Arrays.asList(
                transaction("Matthias", 4, "Jaci"),
                transaction("Matthias", 10, "Thomas"),
                transaction("Thomas", 3, "David"),
                transaction("Thomas", 13, "Thomas B."),
                transaction("David", 13, "Thomas B."),
                transaction("Jaci", 7, "Thomas"),
                transaction("Thomas B.", 6, "Jaci"),
                transaction("Jaci", 5, "Thomas B."),
                transaction("Thomas", 10, "David"),
                transaction("David", 17, "Thomas")
        );

        TransactionListOptimizer optimizerA = new TransactionListOptimizer(transactionListA);
        List<Transaction> optimalTransactionsA = optimizerA.getOptimalTransactions();

        TransactionListOptimizer optimizerB = new TransactionListOptimizer(transactionListB);
        List<Transaction> optimalTransactionsB = optimizerB.getOptimalTransactions();

        assertThat(optimalTransactionsA, equalTo(optimalTransactionsB));
    }


}
