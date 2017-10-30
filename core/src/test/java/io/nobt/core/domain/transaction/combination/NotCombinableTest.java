package io.nobt.core.domain.transaction.combination;

import org.junit.Test;

import static io.nobt.core.domain.transaction.combination.matchers.CombinationResultMatchers.hasChanges;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class NotCombinableTest {

    @Test
    public void shouldHaveNoChanges() throws Exception {
        assertThat(new NotCombinable(), hasChanges(equalTo(false)));
    }

}