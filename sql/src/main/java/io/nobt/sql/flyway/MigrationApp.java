package io.nobt.sql.flyway;

import io.nobt.config.Config;
import io.nobt.config.DatabaseConfig;
import org.flywaydb.core.Flyway;

public class MigrationApp {

    public static void main(String[] args) {

        final Config config = Config.getConfigForCurrentEnvironment();
        final DatabaseConfig databaseConfig = config.getDatabaseConfig();

        Flyway flyway = new Flyway();
        flyway.setDataSource(databaseConfig.url(), databaseConfig.username(), databaseConfig.password());

        flyway.migrate();
    }
}
