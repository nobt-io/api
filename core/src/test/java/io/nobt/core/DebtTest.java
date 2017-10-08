package io.nobt.core;

import io.nobt.core.domain.transaction.Debt;
import io.nobt.core.domain.transaction.combination.CombinationResult;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(JUnitParamsRunner.class)
public class DebtTest {

	@Test
    @Parameters(source = TransactionTestCases.class)
    public void testCombineTransactions(Debt first, Debt second, CombinationResult expected) throws Exception {

        final CombinationResult result = first.combine(second);

        assertThat(result, is(equalTo(expected)));
    }
}
