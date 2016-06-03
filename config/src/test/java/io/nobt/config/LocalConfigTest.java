package io.nobt.config;

import io.nobt.config.LocalConfig;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.postgresql.Driver;

import static org.junit.Assert.*;

public class LocalConfigTest {

    private LocalConfig sut;

    @Before
    public void setUp() throws Exception {
        sut = new LocalConfig();
    }

    @Test
    public void shouldBuildValidPostgresConnectionString() throws Exception {

        sut.initialize();
        final String connectionUrl = sut.getDatabaseConfig().url();

        final boolean isValidUrl = new Driver().acceptsURL(connectionUrl);

        Assert.assertTrue(isValidUrl);
    }
}