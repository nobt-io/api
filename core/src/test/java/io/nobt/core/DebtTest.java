package io.nobt.core;

import io.nobt.core.domain.Debt;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@RunWith(JUnitParamsRunner.class)
public class DebtTest {

	@Test
	@Parameters(source = TransactionTestCases.class)
	public void testCombineTransactions(Debt first, Debt second, Set<Debt> expected) throws Exception {

		final Set<Debt> result = first.combine(second);

		if (expected.isEmpty()) {
			assertThat(result, emptyCollectionOf(Debt.class));
		} else {
			assertThat(result, hasSize(expected.size()));
			assertThat(result, containsInAnyOrder(expected.toArray()));
		}
	}
}
