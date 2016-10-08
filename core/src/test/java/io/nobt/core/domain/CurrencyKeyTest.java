package io.nobt.core.domain;

import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class CurrencyKeyTest {

    @Test
    public void shouldReturnKeyAsUppercase() throws Exception {

        final CurrencyKey sut = new CurrencyKey("eur");

        assertThat(sut.getKey(), equalTo("EUR"));
    }
}