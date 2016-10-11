package io.nobt.persistence;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.util.function.Function;

public class TransactionalNobtRepositoryCommandInvoker implements NobtRepositoryCommandInvoker {

    private static final Logger LOGGER = LogManager.getLogger(TransactionalNobtRepositoryCommandInvoker.class);

    private final EntityManagerFactory entityManagerFactory;
    private final Function<EntityManager, NobtRepository> nobtRepositoryFactory;

    public TransactionalNobtRepositoryCommandInvoker(EntityManagerFactory entityManagerFactory, Function<EntityManager, NobtRepository> nobtRepositoryFactory) {
        this.entityManagerFactory = entityManagerFactory;
        this.nobtRepositoryFactory = nobtRepositoryFactory;
    }

    @Override
    public <T> T invoke(NobtRepositoryCommand<T> command) {

        LOGGER.info("Invoking {}.", command);

        final EntityManager em = entityManagerFactory.createEntityManager();
        final NobtRepository repository = nobtRepositoryFactory.apply(em);

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
}
