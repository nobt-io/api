package io.nobt.rest.config;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class LocalConfig extends Config {

    @Override
    public int getPort() {
        return 8080;
    }

    @Override
    public EntityManagerFactory getEntityManagerFactory() {
        return Persistence.createEntityManagerFactory("local-persistence");
    }
}
