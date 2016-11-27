package io.nobt.core;

import io.nobt.core.domain.Transaction;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static io.nobt.core.domain.Transaction.transaction;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;

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

        Assert.assertThat(optimalTransactions, hasSize(5));
        Assert.assertThat(optimalTransactions, containsInAnyOrder(
                transaction("Matthias", 4, "Thomas"),
                transaction("Jaci", 2, "Thomas B."),
                transaction("Matthias", 10, "Thomas B."),
                transaction("David", 4, "Thomas"),
                transaction("David", 13, "Thomas B.")
                )
        );
    }
}
