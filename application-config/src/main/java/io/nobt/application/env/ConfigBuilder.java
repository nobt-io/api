package io.nobt.application.env;

import io.nobt.persistence.DatabaseConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;
import java.util.function.Function;

import static io.nobt.application.env.Config.Keys.*;

public class ConfigBuilder implements AcceptEnvironment, AcceptOverrides {

    private static final Logger LOGGER = LogManager.getLogger();

    private Environment environment;

    private DatabaseConfig databaseConfig;
    private Integer port;
    private Boolean useInMemoryDatabase;
    private Boolean migrateDatabaseAtStartup;

    public static AcceptEnvironment newInstance() {
        return new ConfigBuilder();
    }

    @Override
    public AcceptOverrides applyEnvironment(Environment environment) {
        this.environment = environment;
        return this;
    }

    @Override
    public AcceptOverrides overridePort(int port) {
        this.port = port;
        return this;
    }

    @Override
    public AcceptOverrides overrideUseInMemoryDatabase(boolean useInMemoryDatabase) {
        this.useInMemoryDatabase = useInMemoryDatabase;
        return this;
    }

    @Override
    public AcceptOverrides overrideMigrateDatabaseAtStartup(boolean migrateDatabaseAtStartup) {
        this.migrateDatabaseAtStartup = migrateDatabaseAtStartup;
        return this;
    }

    @Override
    public AcceptOverrides overrideDatabase(DatabaseConfig databaseConfig) {
        this.databaseConfig = databaseConfig;
        return this;
    }

    @Override
    public Config build() {

        if (port() == null) {
            throw new MissingConfigurationException(PORT);
        }

        if (migrateDatabaseAtStartup() && useInMemoryDatabase()) {
            throw new IllegalConfigurationException(String.format(
                    "Cannot migrate in-memory database. Either pass %s=false or %s=false.",
                    USE_IN_MEMORY_DATABASE,
                    MIGRATE_DATABASE_AT_STARTUP
            ));
        }

        if (migrateDatabaseAtStartup() && databaseConfig() == null) {
            throw new IllegalConfigurationException(String.format(
                    "%s=true requires %s to be present.",
                    MIGRATE_DATABASE_AT_STARTUP,
                    DATABASE_CONNECTION_STRING
            ));
        }

        if (!useInMemoryDatabase() && databaseConfig() == null) {
            throw new IllegalConfigurationException(String.format(
                    "%s=false requires %s to be present.",
                    USE_IN_MEMORY_DATABASE,
                    DATABASE_CONNECTION_STRING
            ));
        }

        return new Config(port(), useInMemoryDatabase(), migrateDatabaseAtStartup(), databaseConfig());
    }

    private DatabaseConfig databaseConfig() {

        if (databaseConfig != null) {
            return databaseConfig;
        }

        return getEnvVariableValue(environment, DATABASE_CONNECTION_STRING, ConnectionStringAdapter::parse).orElse(null);
    }

    private Boolean migrateDatabaseAtStartup() {

        if (migrateDatabaseAtStartup != null) {
            return migrateDatabaseAtStartup;
        }

        return getEnvVariableValue(environment, MIGRATE_DATABASE_AT_STARTUP, Boolean::parseBoolean).orElse(true);
    }

    private Boolean useInMemoryDatabase() {

        if (useInMemoryDatabase != null) {
            return useInMemoryDatabase;
        }

        return getEnvVariableValue(environment, USE_IN_MEMORY_DATABASE, Boolean::parseBoolean).orElse(false);
    }

    private Integer port() {

        if (port != null) {
            return port;
        }

        return getEnvVariableValue(environment, PORT, Integer::parseInt).orElse(null);
    }

    private static <T> Optional<T> getEnvVariableValue(Environment environment, Config.Keys key, Function<String, T> mapper) {

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
}
