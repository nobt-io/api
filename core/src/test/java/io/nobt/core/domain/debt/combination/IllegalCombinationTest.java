package io.nobt.core.domain.debt.combination;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static io.nobt.core.domain.debt.combination.matchers.CombinationResultMatchers.hasChanges;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class IllegalCombinationTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldHaveChanges() throws Exception {
        assertThat(new IllegalCombination(), hasChanges(equalTo(true)));
    }

    @Test
    public void shouldThrowException() throws Exception {

        expectedException.expect(IllegalStateException.class);
        new IllegalCombination().applyTo(null);
    }
}