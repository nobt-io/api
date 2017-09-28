package io.nobt.test.persistence;

import io.nobt.application.env.Config;
import io.nobt.application.env.ConnectionStringAdapter;
import io.nobt.persistence.DatabaseConfig;

public class IntegrationTestDatabaseConfig {

    public static DatabaseConfig create() {
        return ConnectionStringAdapter.parse(System.getenv(Config.Keys.DATABASE_CONNECTION_STRING.name()));
    }

}
