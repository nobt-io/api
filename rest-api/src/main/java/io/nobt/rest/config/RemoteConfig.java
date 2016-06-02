package io.nobt.rest.config;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.pivotal.labs.cfenv.CloudFoundryEnvironment;

public class RemoteConfig extends Config {

    private static final Logger LOGGER = LogManager.getLogger(RemoteConfig.class);

    private final CloudFoundryEnvironment environment;
    private int port;
    private CFConnectionStringAdapter connectionString;

    public RemoteConfig(CloudFoundryEnvironment environment) {
        this.environment = environment;
    }

    @Override
    public void initialize() {
        this.port = Integer.parseInt(System.getenv("PORT"));
        LOGGER.debug("Running on port {}", port);

        final URI databaseUri = getDatabaseURI();
        this.connectionString = CFConnectionStringAdapter.parse(databaseUri);
    }

    public int getDatabasePort() {
        return port;
    }

    @Override
    public DatabaseConfig getDatabaseConfig() {
        return new DatabaseConfig() {
            @Override
            public String url() {
                return connectionString.getUrl();
            }

            @Override
            public String username() {
                return connectionString.getPassword();
            }

            @Override
            public String password() {
                return connectionString.getPassword();
            }
        };
    }

    private URI getDatabaseURI() {
        try {
            return environment.getService("elephant-sql").getUri();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
