package io.nobt.application.env;

import io.nobt.persistence.DatabaseConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import static io.nobt.application.env.Config.Keys.*;

public final class Config {

    private static final Logger LOGGER = LogManager.getLogger();

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

    public static Config from(Environment environment) {

        final Integer port = getEnvVariableValue(environment, PORT, Integer::parseInt).orElse(null);
        final Boolean useInMemoryDatabase = getEnvVariableValue(environment, USE_IN_MEMORY_DATABASE, Boolean::parseBoolean).orElse(false);
        final Boolean migrateDatabaseAtStartUp = getEnvVariableValue(environment, MIGRATE_DATABASE_AT_STARTUP, Boolean::parseBoolean).orElse(true);
        final DatabaseConfig databaseConfig = getEnvVariableValue(environment, DATABASE_CONNECTION_STRING, ConnectionStringAdapter::parse).orElse(null);

        if (port == null) {
            throw new MissingConfigurationException(PORT);
        }

        if (migrateDatabaseAtStartUp && useInMemoryDatabase) {
            throw new IllegalConfigurationException(String.format(
                    "Cannot migrate in-memory database. Either pass %s=false or %s=false.",
                    USE_IN_MEMORY_DATABASE,
                    MIGRATE_DATABASE_AT_STARTUP
            ));
        }

        if (migrateDatabaseAtStartUp && databaseConfig == null) {
            throw new IllegalConfigurationException(String.format(
                    "%s=true requires %s to be present.",
                    MIGRATE_DATABASE_AT_STARTUP,
                    DATABASE_CONNECTION_STRING
            ));
        }

        if (!useInMemoryDatabase && databaseConfig == null) {
            throw new IllegalConfigurationException(String.format(
                    "%s=false requires %s to be present.",
                    USE_IN_MEMORY_DATABASE,
                    DATABASE_CONNECTION_STRING
            ));
        }

        return new Config(port, useInMemoryDatabase, migrateDatabaseAtStartUp, databaseConfig);
    }

    private static <T> Optional<T> getEnvVariableValue(Environment environment, Keys key, Function<String, T> mapper) {

        final String envVariableValue = environment.getValue(key.name());

        return Optional
                .ofNullable(envVariableValue)
                .map(String::trim)
                .map((value) -> {
                    LOGGER.info("{}: {}", key, value);
                    return value;
                })
                .map(mapper);
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
                Objects.equals(databaseConfig, config.databaseConfig);
    }

    @Override
    public int hashCode() {
        return Objects.hash(port, useInMemoryDatabase, migrateDatabaseAtStartUp, databaseConfig);
    }
}
