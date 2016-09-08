package io.nobt.dbconfig.local;

import org.junit.Before;
import org.junit.Test;
import org.postgresql.Driver;

import static org.junit.Assert.assertTrue;

public class LocalDatabaseConfigTest {

    private LocalDatabaseConfig sut;

    @Before
    public void setUp() throws Exception {
        sut = LocalDatabaseConfig.create();
    }

    @Test
    public void shouldBuildValidPostgresConnectionString() throws Exception {

        final String connectionUrl = sut.url();

        final boolean isValidUrl = new Driver().acceptsURL(connectionUrl);

        assertTrue(isValidUrl);
    }
}