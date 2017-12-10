package io.nobt.core.domain;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ShortURLTest {

    @Test
    public void shouldGenerateAnIdOfLength12() throws Exception {

        final String shortUrl = ShortURL.generate();

        assertThat(shortUrl.length(), is(12));
    }
}