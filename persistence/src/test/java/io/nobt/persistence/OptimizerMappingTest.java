package io.nobt.persistence;

import io.nobt.core.optimizer.Optimizer;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class OptimizerMappingTest {

    @Test
    public void stringRepresentationOfAllVariantsMustNotChange() {
        assertThat("all variants should be listed here", Optimizer.values().length, is(equalTo(3)));
        assertThat(Enum.valueOf(Optimizer.class, "MINIMAL_AMOUNT_V1"), is(Optimizer.MINIMAL_AMOUNT_V1));
        assertThat(Enum.valueOf(Optimizer.class, "MINIMAL_AMOUNT_V2"), is(Optimizer.MINIMAL_AMOUNT_V2));
        assertThat(Enum.valueOf(Optimizer.class, "MINIMAL_AMOUNTS_AND_MINIMAL_NUMBER_OF_DEBTS"), is(Optimizer.MINIMAL_AMOUNTS_AND_MINIMAL_NUMBER_OF_DEBTS));
    }

    @Test
    public void noEnumVariantNameIsAbove50Chars() {
        for (Optimizer value : Optimizer.values()) {
            assertThat("the database column is limited to 50 at the moment", value.name().length(), is(not(greaterThan(50))));
        }
    }
}
