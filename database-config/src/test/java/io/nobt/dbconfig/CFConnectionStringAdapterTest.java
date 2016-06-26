package io.nobt.dbconfig;

import org.junit.Assert;
import org.junit.Test;

import java.net.URI;

public class CFConnectionStringAdapterTest {

    @Test
    public void shouldParseGivenConnection() throws Exception {

        final CFConnectionStringAdapter parse = CFConnectionStringAdapter.parse(URI.create("postgres://vpasdipa:aGm32nh2Q2gOE8KjoNvE0w2jYi-b9k4n@pellefant-02.db.elephantsql.com:5432/vpasdipa"));

        Assert.assertEquals("vpasdipa", parse.getUsername());
        Assert.assertEquals("aGm32nh2Q2gOE8KjoNvE0w2jYi-b9k4n", parse.getPassword());
        Assert.assertEquals("jdbc:postgresql://pellefant-02.db.elephantsql.com:5432/vpasdipa", parse.getUrl());
    }
}