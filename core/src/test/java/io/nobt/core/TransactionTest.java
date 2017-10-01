package io.nobt.core;

import io.nobt.core.domain.transaction.Transaction;
import io.nobt.core.domain.transaction.combination.CombinationResult;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(JUnitParamsRunner.class)
public class TransactionTest {

	@Test
	@Parameters(source = TransactionTestCases.class)
	public void testCombineTransactions(Transaction first, Transaction second, CombinationResult expected) throws Exception {

		final CombinationResult result = first.combine(second);

		assertThat(result, is(equalTo(expected)));
	}
}
