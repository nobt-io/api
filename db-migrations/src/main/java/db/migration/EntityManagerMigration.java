package db.migration;

import org.flywaydb.core.api.configuration.ConfigurationAware;
import org.flywaydb.core.api.configuration.FlywayConfiguration;
import org.flywaydb.core.api.migration.jdbc.JdbcMigration;
import org.hibernate.jpa.AvailableSettings;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.sql.DataSource;
import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;

public abstract class EntityManagerMigration<T extends Migratable> implements ConfigurationAware, JdbcMigration {

    private final Class<T> type;

    public EntityManagerMigration() {
        type = ((Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
    }

    private DataSource dataSource;

    @Override
    public void setFlywayConfiguration(FlywayConfiguration flywayConfiguration) {
        dataSource = flywayConfiguration.getDataSource();
    }

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

        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        final CriteriaQuery<T> query = criteriaBuilder.createQuery(type);
        query.select(query.from(type));

        final List<T> entities = entityManager.createQuery(query).getResultList();

        for (final T entity : entities) {

            entity.migrate();

            entityManager.persist(entity);
        }
    }
}
