package io.nobt.sql.flyway;

import org.flywaydb.core.Flyway;

public class MigrationService {

    public void migrateDatabaseAt(String connectionString, String username, String password) {

        Flyway flyway = new Flyway();
        flyway.setDataSource(connectionString, username, password);

        flyway.migrate();
    }
}
