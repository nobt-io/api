package io.nobt.application;

import io.nobt.application.env.Config;
import io.nobt.persistence.*;
import io.nobt.persistence.mapping.ExpenseMapper;
import io.nobt.persistence.mapping.NobtMapper;
import io.nobt.persistence.mapping.ShareMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.EntityManagerFactory;

public class NobtRepositoryCommandInvokerFactory {

    private static final Logger LOGGER = LogManager.getLogger();

    private final Config config;

    public NobtRepositoryCommandInvokerFactory(Config config) {
        this.config = config;
    }

    public NobtRepositoryCommandInvoker create() {

        // make sure that, if unsure we always try to connect to a real database
        if (config.useInMemoryDatabase()) {
            return inMemory();
        } else {
            return transactional();
        }
    }

    private static EntityManagerFactoryProvider entityManagerFactoryProvider = new EntityManagerFactoryProvider();

    public NobtRepositoryCommandInvoker transactional() {

        final DatabaseConfig databaseConfig = config.database();
        final EntityManagerFactory entityManagerFactory = entityManagerFactoryProvider.create(databaseConfig);

        return new TransactionalNobtRepositoryCommandInvoker(entityManagerFactory, (em) -> new NobtRepositoryImpl(em, new NobtMapper(new ExpenseMapper(new ShareMapper()))));
    }

    public NobtRepositoryCommandInvoker inMemory() {
        LOGGER.info("Using In-Memory database.");
        return new InMemoryRepositoryCommandInvoker(new InMemoryNobtRepository());
    }
}
