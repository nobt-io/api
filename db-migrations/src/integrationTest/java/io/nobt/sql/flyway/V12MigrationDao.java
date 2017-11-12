package io.nobt.sql.flyway;

import db.migration.v12.V12Person;
import db.migration.v12.V12_1_NobtEntity;
import io.nobt.persistence.DatabaseConfig;
import io.nobt.persistence.EntityManagerFactoryProvider;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;
import java.util.Set;

public class V12MigrationDao implements AutoCloseable {

    private final EntityManager entityManager;
    private final EntityManagerFactory entityManagerFactory;

    public V12MigrationDao(DatabaseConfig databaseConfig) {
        entityManagerFactory = new EntityManagerFactoryProvider().create("migration", databaseConfig);
        entityManager = entityManagerFactory.createEntityManager();
    }

    public List<V12_1_NobtEntity> getAllNobts() {
        return entityManager.createQuery("SELECT n from V12_1_NobtEntity n", V12_1_NobtEntity.class).getResultList();
    }

    public Set<V12Person> getParticipantsOfNobt(long id) {
        return entityManager
                .createQuery("SELECT n from V12_1_NobtEntity n WHERE n.id = :id", V12_1_NobtEntity.class)
                .setParameter("id", id)
                .getSingleResult()
                .getExplicitParticipants();
    }

    @Override
    public void close() throws Exception {
        entityManager.close();
        entityManagerFactory.close();
    }
}
