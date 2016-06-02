package io.nobt.rest.config;

import java.util.Objects;
import java.util.Properties;

public class LocalConfig extends Config {

    private String databaseHost;
    private String databaseUsername;
    private String databasePassword;
    private String databaseName;
    private String databasePort;

    @Override
    public void initialize() throws Exception {
        final Properties dbProperties = new Properties();
        dbProperties.load(LocalConfig.class.getResourceAsStream("/database.local.properties"));

        databaseHost = dbProperties.getProperty("host");
        databasePassword = dbProperties.getProperty("password");

        databasePort = dbProperties.getProperty("port", "5432");
        databaseName = dbProperties.getProperty("name", "postgres");
        databaseUsername = dbProperties.getProperty("username", "postgres");

        Objects.requireNonNull(databaseHost, "Database-Host must not be null");
        Objects.requireNonNull(databasePort, "Database-Port must not be null");
        Objects.requireNonNull(databaseName, "Database-name must not be null");
        Objects.requireNonNull(databasePassword, "Database-Password must not be null");
        Objects.requireNonNull(databaseUsername, "Database-username must not be null");
    }

    public int getDatabasePort() {
        return 8080;
    }

    @Override
    public DatabaseConfig getDatabaseConfig() {

        return new DatabaseConfig() {
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
        };
    }
}
