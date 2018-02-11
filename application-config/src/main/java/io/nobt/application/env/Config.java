package io.nobt.application.env;

import io.nobt.persistence.DatabaseConfig;

import java.util.Objects;

public final class Config {

    private final Integer port;
    private final Boolean useInMemoryDatabase;
    private final Boolean migrateDatabaseAtStartUp;
    private final DatabaseConfig databaseConfig;
    private final String schemeOverrideHeader;
    public Config(Integer port, Boolean useInMemoryDatabase, Boolean migrateDatabaseAtStartUp, DatabaseConfig databaseConfig, String schemeOverrideHeader) {
        this.port = port;
        this.useInMemoryDatabase = useInMemoryDatabase;
        this.migrateDatabaseAtStartUp = migrateDatabaseAtStartUp;
        this.databaseConfig = databaseConfig;
        this.schemeOverrideHeader = schemeOverrideHeader;
    }

    public String schemeOverrideHeader() {
        return schemeOverrideHeader;
    }

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
                Objects.equals(databaseConfig, config.databaseConfig) &&
                Objects.equals(schemeOverrideHeader, config.schemeOverrideHeader);
    }

    @Override
    public int hashCode() {
        return Objects.hash(port, useInMemoryDatabase, migrateDatabaseAtStartUp, databaseConfig, schemeOverrideHeader);
    }

    public enum Keys {
        PORT,
        DATABASE_CONNECTION_STRING,
        USE_IN_MEMORY_DATABASE,
        MIGRATE_DATABASE_AT_STARTUP,
        SCHEME_OVERRIDE_HEADER
    }
}
