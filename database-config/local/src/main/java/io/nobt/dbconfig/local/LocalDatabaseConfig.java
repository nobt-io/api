package io.nobt.dbconfig.local;

import io.nobt.persistence.DatabaseConfig;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Objects;
import java.util.Properties;

public class LocalDatabaseConfig implements DatabaseConfig {

    private String databaseHost;
    private String databaseUsername;
    private String databasePassword;
    private String databaseName;
    private String databasePort;

    private LocalDatabaseConfig() {
    }

    public static LocalDatabaseConfig create() {
        final Properties dbProperties = new Properties();

        try {
            dbProperties.load(LocalDatabaseConfig.class.getResourceAsStream("/database.local.properties"));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        String databaseHost = dbProperties.getProperty("host");
        String databasePassword = dbProperties.getProperty("password");

        String databasePort = dbProperties.getProperty("port", "5432");
        String databaseName = dbProperties.getProperty("name", "postgres");
        String databaseUsername = dbProperties.getProperty("username", "postgres");

        Objects.requireNonNull(databaseHost, "Database-Host must not be null");
        Objects.requireNonNull(databasePassword, "Database-Password must not be null");

        final LocalDatabaseConfig config = new LocalDatabaseConfig();
        config.databaseHost = databaseHost;
        config.databasePassword = databasePassword;
        config.databasePort = databasePort;
        config.databaseName = databaseName;
        config.databaseUsername = databaseUsername;

        return config;
    }

    @Override
    public String url() {
        return String.format("jdbc:postgresql://%s:%s/%s", databaseHost, databasePort, databaseName);
    }

    @Override
    public String username() {
        return databaseUsername;
    }

    @Override
    public String password() {
        return databasePassword;
    }
}
