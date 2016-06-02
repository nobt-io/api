package io.nobt.rest;

import io.nobt.rest.config.DatabaseConfig;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashMap;

public class EntityManagerFactoryProvider {

    public EntityManagerFactory create(DatabaseConfig config) {

        final HashMap<String, String> properties = new HashMap<String, String>() {{
            put("javax.persistence.jdbc.url", config.url());
            put("javax.persistence.jdbc.username", config.username());
            put("javax.persistence.jdbc.password", config.password());
        }};

        return Persistence.createEntityManagerFactory("persistence", properties);
    }
}
