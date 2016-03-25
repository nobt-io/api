package io.nobt.core;

import io.nobt.core.domain.Transaction;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@RunWith(JUnitParamsRunner.class)
public class TransactionTest {

	@Test
	@Parameters(source = TransactionTestCases.class)
	public void testCombineTransactions(Transaction first, Transaction second, Set<Transaction> expected) throws Exception {

		final Set<Transaction> result = first.combine(second);

		if (expected.isEmpty()) {
			assertThat(result, emptyCollectionOf(Transaction.class));
		} else {
			assertThat(result, hasSize(expected.size()));
			assertThat(result, containsInAnyOrder(expected.toArray()));
		}
	}
}
