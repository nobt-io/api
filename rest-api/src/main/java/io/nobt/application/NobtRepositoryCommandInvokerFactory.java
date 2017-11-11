package io.nobt.application;

import io.nobt.application.env.Config;
import io.nobt.persistence.InMemoryNobtRepository;
import io.nobt.persistence.InMemoryRepositoryCommandInvoker;
import io.nobt.persistence.NobtRepositoryCommandInvoker;
import io.nobt.persistence.TransactionalNobtRepositoryCommandInvoker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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


    private NobtRepositoryCommandInvoker transactional() {
        return TransactionalNobtRepositoryCommandInvoker.forDatabaseConfig(config.database());
    }

    private NobtRepositoryCommandInvoker inMemory() {
        LOGGER.info("Using In-Memory database.");
        return new InMemoryRepositoryCommandInvoker(new InMemoryNobtRepository());
    }
}
