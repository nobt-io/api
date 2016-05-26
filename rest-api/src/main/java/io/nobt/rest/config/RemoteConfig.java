package io.nobt.rest.config;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.pivotal.labs.cfenv.CloudFoundryEnvironment;

public class RemoteConfig extends Config {

    private static final Logger LOGGER = LogManager.getLogger(RemoteConfig.class);
    private final CloudFoundryEnvironment environment;

    public RemoteConfig(CloudFoundryEnvironment environment) {
        this.environment = environment;
    }

    @Override
    public int getPort() {
        final int port = Integer.parseInt(System.getenv("PORT"));

        LOGGER.debug("Running on port {}", port);

        return port;
    }

    @Override
    public EntityManagerFactory getEntityManagerFactory() {
        return Persistence.createEntityManagerFactory("remote-persistence", readPropertiesFromEnvVariables());
    }

    private Map readPropertiesFromEnvVariables() {

        final URI databaseUri = getDatabaseURI();
        final ConnectionString connectionString = ConnectionString.parse(databaseUri);

        return new HashMap() {{
            put("javax.persistence.jdbc.user", connectionString.getUsername());
            put("javax.persistence.jdbc.password", connectionString.getPassword());
            put("javax.persistence.jdbc.url", connectionString.getUrl());
        }};
    }

    private URI getDatabaseURI() {
        try {
            return environment.getService("elephant-sql").getUri();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
