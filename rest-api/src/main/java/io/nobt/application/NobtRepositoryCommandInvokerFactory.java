package io.nobt.application;

import io.nobt.application.env.Config;
import io.nobt.persistence.*;
import io.nobt.persistence.mapping.ExpenseMapper;
import io.nobt.persistence.mapping.NobtMapper;
import io.nobt.persistence.mapping.ShareMapper;

import javax.persistence.EntityManagerFactory;

import static io.nobt.application.env.Config.Keys.DATABASE_CONNECTION_STRING;
import static io.nobt.application.env.MissingConfigurationException.missingConfigurationException;

public class NobtRepositoryCommandInvokerFactory {

    public NobtRepositoryCommandInvoker create() {

        // make sure that, if unsure we always try to connect to a real database
        if (Config.useInMemoryDatabase().orElse(false)) {
            return inMemory();
        } else {
            return transactional();
        }
    }

    private static EntityManagerFactoryProvider entityManagerFactoryProvider = new EntityManagerFactoryProvider();

    public static NobtRepositoryCommandInvoker transactional() {

        final DatabaseConfig config = Config.database().orElseThrow(missingConfigurationException(DATABASE_CONNECTION_STRING));
        final EntityManagerFactory entityManagerFactory = entityManagerFactoryProvider.create(config);

        return new TransactionalNobtRepositoryCommandInvoker(entityManagerFactory, (em) -> new NobtRepositoryImpl(em, new NobtMapper(new ExpenseMapper(new ShareMapper()))));
    }

    public static NobtRepositoryCommandInvoker inMemory() {
        return new InMemoryRepositoryCommandInvoker(new InMemoryNobtRepository());
    }
}
