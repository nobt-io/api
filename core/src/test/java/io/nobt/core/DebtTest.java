package io.nobt.core;

import io.nobt.core.domain.debt.Debt;
import io.nobt.core.domain.debt.combination.CombinationResult;
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
    @Parameters(source = DebtTestCases.class)
    public void testCombineDebts(Debt first, Debt second, CombinationResult expected) throws Exception {

        final CombinationResult result = first.combine(second);

        assertThat(result, is(equalTo(expected)));
    }
}
