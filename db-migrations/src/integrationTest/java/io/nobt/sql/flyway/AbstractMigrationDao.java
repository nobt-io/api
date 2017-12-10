package io.nobt.sql.flyway;

import io.nobt.persistence.DatabaseConfig;
import io.nobt.persistence.EntityManagerFactoryProvider;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.util.function.Function;

public class AbstractMigrationDao implements AutoCloseable {

    private final EntityManager entityManager;
    private final EntityManagerFactory entityManagerFactory;

    public AbstractMigrationDao(DatabaseConfig databaseConfig) {
        entityManagerFactory = new EntityManagerFactoryProvider().create("migration", databaseConfig);
        entityManager = entityManagerFactory.createEntityManager();
    }

    protected <T> T doInTx(Function<EntityManager, T> function) {

        final EntityTransaction tx = entityManager.getTransaction();

        tx.begin();

        final T result;

        try {
            result = function.apply(entityManager);
            entityManager.flush();
            tx.commit();
            entityManager.clear();
        } catch (Exception e) {
            tx.rollback();
            throw e;
        }

        return result;
    }

    @Override
    public void close() throws Exception {
        entityManager.close();
        entityManagerFactory.close();
    }
}
