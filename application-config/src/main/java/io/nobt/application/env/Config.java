package io.nobt.application.env;

import io.nobt.persistence.DatabaseConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;
import java.util.function.Function;

import static io.nobt.application.env.Config.Keys.*;

/**
 * Configuration class for the application.
 * All public getters return an {@link Optional} because not all environment variables may have been set and resolution of this conflict is handed to the client
 * because some clients (e.g. tests) may not care about some variables being unset.
 */
public final class Config {

    public enum Keys {
        PORT,
        DATABASE_CONNECTION_STRING,
        REPORT_SERVER_ERRORS_AS_ISSUES,
        USE_IN_MEMORY_DATABASE,
        WRITE_STACKTRACE_TO_RESPONSE,
        MIGRATE_DATABASE_AT_STARTUP
    }

    private static final Logger LOGGER = LogManager.getLogger(Config.class);

    private static Config instance;

    private Integer port;
    private Boolean shouldWriteStacktraceToResponse;
    private Boolean shouldReportServerErrorsAsIssues;
    private Boolean shouldUseInMemoryDatabase;
    private Boolean migrateDatabaseAtStartUp;
    private Lazy<DatabaseConfig> databaseConnectionString;

    public static Optional<Integer> port() {
        return Optional.ofNullable(getInstance().port);
    }

    public static Optional<Boolean> writeStacktraceToResponse() {
        return Optional.ofNullable(getInstance().shouldWriteStacktraceToResponse);
    }

    public static Optional<Boolean> reportServerErrorsAsIssues() {
        return Optional.ofNullable(getInstance().shouldReportServerErrorsAsIssues);
    }

    public static Optional<Boolean> useInMemoryDatabase() {
        return Optional.ofNullable(getInstance().shouldUseInMemoryDatabase);
    }

    public static Optional<Boolean> migrateDatabaseAtStartUp() {
        return Optional.ofNullable(getInstance().migrateDatabaseAtStartUp);
    }

    public static Optional<DatabaseConfig> database() {
        return Optional.ofNullable(getInstance().databaseConnectionString.get());
    }

    private static Config getInstance() {

        if (instance == null) {

            instance = new Config();

            instance.port = getEnv(PORT, Integer::parseInt);
            instance.shouldReportServerErrorsAsIssues = getEnv(REPORT_SERVER_ERRORS_AS_ISSUES, Boolean::parseBoolean);
            instance.shouldUseInMemoryDatabase = getEnv(USE_IN_MEMORY_DATABASE, Boolean::parseBoolean);
            instance.shouldWriteStacktraceToResponse = getEnv(WRITE_STACKTRACE_TO_RESPONSE, Boolean::parseBoolean);
            instance.migrateDatabaseAtStartUp = getEnv(MIGRATE_DATABASE_AT_STARTUP, Boolean::parseBoolean);
            instance.databaseConnectionString = new Lazy<>(() -> getEnv(DATABASE_CONNECTION_STRING, CFConnectionStringAdapter::parse));
        }

        return instance;
    }

    private static <T> T getEnv(Keys key, Function<String, T> mapper) {
        return Optional.ofNullable(System.getenv(key.name()))
                .map(String::trim)
                .map(mapper)
                .map((value) -> {
                    LOGGER.info("{}: {}", key, value);
                    return value;
                })
                .orElse(null);
    }
}
