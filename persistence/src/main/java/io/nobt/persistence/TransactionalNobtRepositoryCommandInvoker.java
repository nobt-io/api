package io.nobt.persistence;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.io.IOException;

public class TransactionalNobtRepositoryCommandInvoker implements NobtRepositoryCommandInvoker {

    private static final Logger LOGGER = LogManager.getLogger(TransactionalNobtRepositoryCommandInvoker.class);

    private final EntityManagerFactory entityManagerFactory;
    private final EntityManagerNobtRepositoryFactory nobtRepositoryFactory;

    public TransactionalNobtRepositoryCommandInvoker(EntityManagerFactory entityManagerFactory) {
        this(entityManagerFactory, new DefaultEntityManagerNobtRepositoryFactory());
    }

    public TransactionalNobtRepositoryCommandInvoker(EntityManagerFactory entityManagerFactory, EntityManagerNobtRepositoryFactory entityManagerNobtRepositoryFactory) {
        this.entityManagerFactory = entityManagerFactory;
        this.nobtRepositoryFactory = entityManagerNobtRepositoryFactory;
    }

    public static TransactionalNobtRepositoryCommandInvoker forDatabaseConfig(DatabaseConfig databaseConfig) {

        final EntityManagerFactoryProvider entityManagerFactoryProvider = new EntityManagerFactoryProvider();
        final EntityManagerFactory entityManagerFactory = entityManagerFactoryProvider.create(databaseConfig);

        return new TransactionalNobtRepositoryCommandInvoker(entityManagerFactory);
    }

    @Override
    public <T> T invoke(NobtRepositoryCommand<T> command) {

        LOGGER.info("Invoking {}.", command);

        final EntityManager em = entityManagerFactory.createEntityManager();
        final EntityManagerNobtRepository repository = nobtRepositoryFactory.create(em);

        final EntityTransaction transaction = em.getTransaction();

        transaction.begin();

        final T result;

        try {
            result = command.execute(repository);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        } finally {
            em.close();
        }

        return result;
    }

    @Override
    public void close() throws IOException {
        entityManagerFactory.close();
    }
}
