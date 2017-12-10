package db.migration;

import org.flywaydb.core.api.configuration.ConfigurationAware;
import org.flywaydb.core.api.configuration.FlywayConfiguration;
import org.flywaydb.core.api.migration.jdbc.JdbcMigration;
import org.hibernate.jpa.AvailableSettings;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.sql.DataSource;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;

public abstract class EntityManagerMigration<T> implements ConfigurationAware, JdbcMigration {

    private DataSource dataSource;

    @Override
    public void setFlywayConfiguration(FlywayConfiguration flywayConfiguration) {
        dataSource = flywayConfiguration.getDataSource();
    }

    protected abstract List<T> retrieveEntities(EntityManager entityManager);

    protected abstract T performUpdate(T entity);

    @Override
    public void migrate(Connection connection) throws Exception {

        final HashMap properties = new HashMap();
        properties.put(AvailableSettings.NON_JTA_DATASOURCE, dataSource);

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("migration", properties);

        final EntityManager entityManager = emf.createEntityManager();
        final EntityTransaction tx = entityManager.getTransaction();

        tx.begin();
        try {
            doBatchUpdate(entityManager);
            entityManager.flush();
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally {
            entityManager.close();
            emf.close();
        }
    }

    private void doBatchUpdate(EntityManager entityManager) {

        final List<T> entities = retrieveEntities(entityManager);

        for (final T entity : entities) {

            final T newEntity = performUpdate(entity);

            entityManager.persist(newEntity);
        }
    }
}
