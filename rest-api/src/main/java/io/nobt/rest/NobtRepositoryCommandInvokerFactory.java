package io.nobt.rest;

import io.nobt.persistence.*;
import io.nobt.persistence.mapping.ExpenseMapper;
import io.nobt.persistence.mapping.NobtMapper;
import io.nobt.persistence.mapping.ShareMapper;

import javax.persistence.EntityManagerFactory;

public class NobtRepositoryCommandInvokerFactory {

    private static EntityManagerFactoryProvider entityManagerFactoryProvider = new EntityManagerFactoryProvider();

    public static NobtRepositoryCommandInvoker transactional(DatabaseConfig databaseConfig) {
        final EntityManagerFactory entityManagerFactory = entityManagerFactoryProvider.create(databaseConfig);

        return new TransactionalNobtRepositoryCommandInvoker(entityManagerFactory, (em) -> new NobtRepositoryImpl(em, new NobtMapper(new ExpenseMapper(new ShareMapper()))));
    }

    public static NobtRepositoryCommandInvoker inMemory() {
        return new InMemoryRepositoryCommandInvoker(new InMemoryNobtRepository());
    }
}
