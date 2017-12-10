package io.nobt.core.domain;

import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class AmountTest {

    @Test
    public void shouldCorrectlyDevideValue() throws Exception {

        final Amount amount = Amount.fromDouble(10);

        final Amount result = amount.divideBy(BigDecimal.valueOf(2));

        assertThat(result, equalTo(Amount.fromDouble(5)));
    }
}