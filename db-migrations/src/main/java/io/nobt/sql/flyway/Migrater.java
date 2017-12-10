package io.nobt.sql.flyway;

import io.nobt.persistence.DatabaseConfig;

public class Migrater {

    public static void main(String[] args) {
        new MigrationService(new DatabaseConfig() {
            @Override
            public String url() {
                return "jdbc:postgresql://localhost:6543/postgres";
            }

            @Override
            public String username() {
                return "postgres";
            }

            @Override
            public String password() {
                return "password";
            }
        }).migrate();
    }

}
