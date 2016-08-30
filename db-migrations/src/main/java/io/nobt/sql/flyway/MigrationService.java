package io.nobt.sql.flyway;

import io.nobt.persistence.DatabaseConfig;
import org.flywaydb.core.Flyway;

public class MigrationService {

    public void migrateDatabaseAt(DatabaseConfig databaseConfig) {

        Flyway flyway = new Flyway();
        flyway.setDataSource(databaseConfig.url(), databaseConfig.username(), databaseConfig.password());

        flyway.migrate();
    }
}
