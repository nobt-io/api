package io.nobt.test.persistence;

import io.nobt.persistence.DatabaseConfig;

import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseAvailabilityCheck {

    private final DatabaseConfig config;

    public DatabaseAvailabilityCheck(DatabaseConfig config) {
        this.config = config;
    }

    public boolean isDatabaseUp() {

        try {
            DriverManager.setLoginTimeout(1);
            DriverManager.getConnection(config.url(), config.username(), config.password());
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
}
