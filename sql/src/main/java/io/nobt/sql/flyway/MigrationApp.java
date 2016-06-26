package io.nobt.sql.flyway;

import io.nobt.dbconfig.CloudDatabaseConfig;
import io.nobt.dbconfig.DatabaseConfig;
import io.nobt.dbconfig.LocalDatabaseConfig;
import io.nobt.profiles.Profile;
import org.flywaydb.core.Flyway;

public class MigrationApp {

    public static void main(String[] args) {

        final DatabaseConfig databaseConfig = Profile.getCurrentProfile().getProfileDependentValue(() -> null, LocalDatabaseConfig::create, CloudDatabaseConfig::create);

        Flyway flyway = new Flyway();
        flyway.setDataSource(databaseConfig.url(), databaseConfig.username(), databaseConfig.password());

        flyway.migrate();
    }
}
