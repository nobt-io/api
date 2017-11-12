package io.nobt.sql.flyway;

import io.nobt.persistence.DatabaseConfig;
import org.flywaydb.core.Flyway;

public class MigrationService {

    private final Flyway flyway;

    public MigrationService(DatabaseConfig config) {
        this.flyway = new Flyway();
        this.flyway.setDataSource(config.url(), config.username(), config.password());
    }

    public void migrate() {
        flyway.migrate();
    }

    public void clean() {
        flyway.clean();
    }

    public void setTargetVersion(String version) {
        flyway.setTargetAsString(version);
    }
}
