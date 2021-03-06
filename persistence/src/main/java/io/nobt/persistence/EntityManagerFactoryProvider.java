package io.nobt.persistence;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashMap;

public class EntityManagerFactoryProvider {

    public EntityManagerFactory create(DatabaseConfig config) {
        return create("persistence", config);
    }

    public EntityManagerFactory create(String persistenceUnitName, DatabaseConfig config) {

        final HashMap<String, String> properties = new HashMap<String, String>() {{
            put("javax.persistence.jdbc.url", config.url());
            put("javax.persistence.jdbc.user", config.username());
            put("javax.persistence.jdbc.password", config.password());
        }};

        return Persistence.createEntityManagerFactory(persistenceUnitName, properties);
    }
}
