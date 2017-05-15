package io.nobt.application.env;

import io.nobt.persistence.DatabaseConfig;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ConnectionStringAdapterTest {

    @Test
    public void shouldParseGivenConnection() throws Exception {

        final String connectionUriAsGivenByCF = "postgres://vpasdipa:aGm32nh2Q2gOE8KjoNvE0w2jYi-b9k4n@pellefant-02.db.elephantsql.com:5432/vpasdipa";

        final DatabaseConfig config = ConnectionStringAdapter.parse(connectionUriAsGivenByCF);

        assertEquals("vpasdipa", config.username());
        assertEquals("aGm32nh2Q2gOE8KjoNvE0w2jYi-b9k4n", config.password());
        assertEquals("jdbc:postgresql://pellefant-02.db.elephantsql.com:5432/vpasdipa", config.url());
    }

    @Test
    public void shouldNotTemperAlreadyCorrectConnectionString() throws Exception {

        final String validConnectionString = "jdbc:postgresql://postgres:password@postgres:5432/postgres";

        final DatabaseConfig config = ConnectionStringAdapter.parse(validConnectionString);

        assertEquals("postgres", config.username());
        assertEquals("password", config.password());
        assertEquals("jdbc:postgresql://postgres:5432/postgres", config.url());
    }
}