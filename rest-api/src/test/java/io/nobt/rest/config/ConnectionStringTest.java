package io.nobt.rest.config;

import java.net.URI;

import org.junit.Assert;
import org.junit.Test;

public class ConnectionStringTest {

    @Test
    public void shouldParseGivenConnection() throws Exception {

        final ConnectionString parse = ConnectionString.parse(URI.create("postgres://gohskslp:asdf@pellefant-02.db.elephantsql.com:5432/gohskslp"));

        Assert.assertEquals("gohskslp", parse.getUsername());
        Assert.assertEquals("asdf", parse.getPassword());
        Assert.assertEquals("jdbc:postgresql://pellefant-02.db.elephantsql.com:5432/gohskslp", parse.getUrl());
    }
}