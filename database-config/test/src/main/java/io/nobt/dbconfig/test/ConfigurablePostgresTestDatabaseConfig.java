package io.nobt.dbconfig.test;

import java.util.Optional;

public class ConfigurablePostgresTestDatabaseConfig implements TestDatabaseConfig {

    public static final String PORT_KEY = "TEST_POSTGRES_PORT";
    public static final String HOST_KEY = "TEST_POSTGRES_HOST";
    public static final String USER_KEY = "TEST_POSTGRES_USER";
    public static final String PASSWORD_KEY = "TEST_POSTGRES_PASSWORD";

    private final Integer port;
    private final String host;
    private final String username;
    private final String password;

    private ConfigurablePostgresTestDatabaseConfig(Integer port, String host, String username, String password) {
        this.port = port;
        this.host = host;
        this.username = username;
        this.password = password;
    }

    public static TestDatabaseConfig parse(EnvVariableSupplier supplier) {

        final int port = supplier.tryGet(PORT_KEY).map(Integer::parseInt).orElse(5432);
        final String host = supplier.tryGet(HOST_KEY).orElse("localhost");
        final String username = supplier.tryGet(USER_KEY).orElse("postgres");
        final String password = supplier.tryGet(PASSWORD_KEY).orElse("password");

        return new ConfigurablePostgresTestDatabaseConfig(port, host, username, password);
    }

    @Override
    public Integer port() {
        return port;
    }

    @Override
    public String url() {
        return String.format("jdbc:postgresql://%s:%d/postgres", host, port());
    }

    @Override
    public String username() {
        return username;
    }

    @Override
    public String password() {
        return password;
    }

    public interface EnvVariableSupplier {

        String get(String key);

        default Optional<String> tryGet(String key) {
            return Optional.ofNullable(get(key));
        }
    }
}
