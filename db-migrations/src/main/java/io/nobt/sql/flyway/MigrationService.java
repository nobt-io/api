package io.nobt.sql.flyway;

import io.nobt.persistence.DatabaseConfig;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationVersion;

public class MigrationService {

    private final Flyway flyway;

    public MigrationService(DatabaseConfig config) {
        this.flyway = new Flyway();
        this.flyway.setDataSource(config.url(), config.username(), config.password());
    }

    public void migrate() {
        migrate(MigrationVersion.LATEST);
    }

    public void migrate(String version) {
        migrate(MigrationVersion.fromVersion(version));
    }

    public void clean() {
        flyway.clean();
    }

    private void migrate(MigrationVersion migrationVersion) {
        flyway.setTarget(migrationVersion);
        flyway.migrate();
    }
}
