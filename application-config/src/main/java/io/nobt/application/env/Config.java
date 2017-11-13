package io.nobt.application.env;

import io.nobt.persistence.DatabaseConfig;

import java.util.Objects;

public final class Config {

    public enum Keys {
        PORT,
        DATABASE_CONNECTION_STRING,
        USE_IN_MEMORY_DATABASE,
        MIGRATE_DATABASE_AT_STARTUP
    }

    public Config(Integer port, Boolean useInMemoryDatabase, Boolean migrateDatabaseAtStartUp, DatabaseConfig databaseConfig) {
        this.port = port;
        this.useInMemoryDatabase = useInMemoryDatabase;
        this.migrateDatabaseAtStartUp = migrateDatabaseAtStartUp;
        this.databaseConfig = databaseConfig;
    }

    private Integer port;
    private Boolean useInMemoryDatabase;
    private Boolean migrateDatabaseAtStartUp;
    private DatabaseConfig databaseConfig;

    public Integer port() {
        return port;
    }

    public Boolean useInMemoryDatabase() {
        return useInMemoryDatabase;
    }

    public Boolean migrateDatabaseAtStartUp() {
        return migrateDatabaseAtStartUp;
    }

    public DatabaseConfig database() {
        return databaseConfig;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Config config = (Config) o;
        return Objects.equals(port, config.port) &&
                Objects.equals(useInMemoryDatabase, config.useInMemoryDatabase) &&
                Objects.equals(migrateDatabaseAtStartUp, config.migrateDatabaseAtStartUp) &&
                Objects.equals(databaseConfig, config.databaseConfig);
    }

    @Override
    public int hashCode() {
        return Objects.hash(port, useInMemoryDatabase, migrateDatabaseAtStartUp, databaseConfig);
    }
}
