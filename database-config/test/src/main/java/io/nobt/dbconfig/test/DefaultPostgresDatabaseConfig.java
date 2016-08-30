package io.nobt.dbconfig.test;

import io.nobt.persistence.DatabaseConfig;

public class DefaultPostgresDatabaseConfig implements DatabaseConfig {

    public Integer port() {
        return 5432;
    }

    @Override
    public String url() {
        return String.format("jdbc:postgresql://localhost:%d/postgres", port());
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
