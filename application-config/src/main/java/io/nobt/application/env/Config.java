package io.nobt.application.env;

import io.nobt.persistence.DatabaseConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;
import java.util.function.Function;

import static io.nobt.application.env.Config.Keys.*;
import static io.nobt.application.env.MissingConfigurationException.missingConfigurationException;

public final class Config {

    public enum Keys {
        PORT,
        DATABASE_CONNECTION_STRING,
        USE_IN_MEMORY_DATABASE,
        MIGRATE_DATABASE_AT_STARTUP
    }

    private static final Logger LOGGER = LogManager.getLogger(Config.class);

    private static Config instance;

    private Integer port;
    private Boolean shouldUseInMemoryDatabase;
    private Boolean migrateDatabaseAtStartUp;
    private Lazy<DatabaseConfig> databaseConnectionString;

    public static Optional<Integer> port() {
        return Optional.ofNullable(getInstance().port);
    }

    /**
     * Whether or not to use the built-in in-memory database.
     * Defaults to false in order to ease production usage.
     */
    public static boolean useInMemoryDatabase() {
        return Optional
                .ofNullable(getInstance().shouldUseInMemoryDatabase)
                .orElse(false);
    }

    /**
     * Whether or not to use migrate the database on startup.
     * Defaults to true in order to ease production usage.
     */
    public static Boolean migrateDatabaseAtStartUp() {
        return Optional
                .ofNullable(getInstance().migrateDatabaseAtStartUp)
                .orElse(true);
    }

    public static DatabaseConfig database() {
        return Optional
                .ofNullable(getInstance().databaseConnectionString.get())
                .orElseThrow(missingConfigurationException(DATABASE_CONNECTION_STRING));
    }

    private static Config getInstance() {

        if (instance == null) {

            instance = new Config();

            instance.port = getEnv(PORT, Integer::parseInt);
            instance.shouldUseInMemoryDatabase = getEnv(USE_IN_MEMORY_DATABASE, Boolean::parseBoolean);
            instance.migrateDatabaseAtStartUp = getEnv(MIGRATE_DATABASE_AT_STARTUP, Boolean::parseBoolean);
            instance.databaseConnectionString = new Lazy<>(() -> getEnv(DATABASE_CONNECTION_STRING, ConnectionStringAdapter::parse));
        }

        return instance;
    }

    private static <T> T getEnv(Keys key, Function<String, T> mapper) {
        return Optional.ofNullable(System.getenv(key.name()))
                .map(String::trim)
                .map((value) -> {
                    LOGGER.info("{}: {}", key, value);
                    return value;
                })
                .map(mapper)
                .orElse(null);
    }
}
