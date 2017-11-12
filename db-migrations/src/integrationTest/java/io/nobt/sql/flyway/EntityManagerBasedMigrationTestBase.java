package io.nobt.sql.flyway;

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

public abstract class EntityManagerBasedMigrationTestBase<T> implements ConfigurationAware, JdbcMigration {

    private DataSource dataSource;

    @Override
    public void setFlywayConfiguration(FlywayConfiguration flywayConfiguration) {
        dataSource = flywayConfiguration.getDataSource();
    }

    protected void assertState(EntityManager entityManager) {

    }

    @Override
    public void migrate(Connection connection) throws Exception {

        final HashMap properties = new HashMap();
        properties.put(AvailableSettings.NON_JTA_DATASOURCE, dataSource);

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("migration", properties);

        final EntityManager entityManager = emf.createEntityManager();
        final EntityTransaction tx = entityManager.getTransaction();

        try {
            assertState(entityManager);
        } finally {
            entityManager.close();
            emf.close();
        }
    }

}
