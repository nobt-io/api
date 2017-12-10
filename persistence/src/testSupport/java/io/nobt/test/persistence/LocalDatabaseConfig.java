package io.nobt.test.persistence;

import io.nobt.persistence.DatabaseConfig;

public class LocalDatabaseConfig implements DatabaseConfig {

    private final int exposedPort;

    public LocalDatabaseConfig(int exposedPort) {
        this.exposedPort = exposedPort;
    }

    @Override
    public String url() {
        return "jdbc:postgresql://localhost:" + exposedPort + "/postgres";
    }

    @Override
    public String username() {
        return "postgres";
    }

    @Override
    public String password() {
        return "password";
    }
}
