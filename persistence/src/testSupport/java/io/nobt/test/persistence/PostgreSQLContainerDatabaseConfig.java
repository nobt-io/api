package io.nobt.test.persistence;

import io.nobt.persistence.DatabaseConfig;
import org.testcontainers.containers.PostgreSQLContainer;

public class PostgreSQLContainerDatabaseConfig implements DatabaseConfig {

    private final PostgreSQLContainer postgreSQLContainer;

    public PostgreSQLContainerDatabaseConfig(PostgreSQLContainer postgreSQLContainer) {
        this.postgreSQLContainer = postgreSQLContainer;
    }

    @Override
    public String url() {
        return postgreSQLContainer.getJdbcUrl();
    }

    @Override
    public String username() {
        return postgreSQLContainer.getUsername();
    }

    @Override
    public String password() {
        return postgreSQLContainer.getPassword();
    }
}
