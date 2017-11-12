package db.migration.v12_1_1;

import db.migration.v12.Nobt;
import db.migration.v12.NobtRowMapper;
import db.migration.v12.V12Person;
import org.flywaydb.core.api.configuration.ConfigurationAware;
import org.flywaydb.core.api.configuration.FlywayConfiguration;
import org.flywaydb.core.api.migration.MigrationChecksumProvider;
import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.hibernate.jpa.AvailableSettings;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * This class is picked up by Flyway as part of the integration test of migrations because it is only on the classpath of integration tests.
 * <p>
 * The basic idea of this test is simple:
 * <p>
 * Can the JSON that was converted by our migration be read through JPA?
 * In order to test this, we need a few things:
 * <p>
 * - An EntityManager
 * - The correct JSON configuration for our domain objects.
 * <p>
 * We access the entity manager through the {@link DataSource} of {@link FlywayConfiguration} which is given to us by the {@link ConfigurationAware} interface.
 * <p>
 * The JSON configuration is copied(!) from the original configuration. This is necessary, because the migrations have to test the settings at the point in time they were created.
 * The current configuration may change later but the migration still has to work. Therefore, a migration needs to have all the things it needs locally.
 */
public class V12_1_1__Test_Fetching_of_migrated_participants implements SpringJdbcMigration, MigrationChecksumProvider, ConfigurationAware {

    private DataSource dataSource;

    @Override
    public void setFlywayConfiguration(FlywayConfiguration flywayConfiguration) {
        dataSource = flywayConfiguration.getDataSource();
    }

    @Override
    public Integer getChecksum() {
        return V12_1_1__Test_Fetching_of_migrated_participants.class.getName().hashCode();
    }

    @Override
    public void migrate(JdbcTemplate jdbcTemplate) throws Exception {

        final HashMap properties = new HashMap();
        properties.put(AvailableSettings.NON_JTA_DATASOURCE, dataSource);

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence", properties);

        final EntityManager entityManager = emf.createEntityManager();
        final List<Nobt> nobts = jdbcTemplate.query("SELECT id,explicitParticipants_legacy FROM nobts", new NobtRowMapper());

        for (Nobt nobt : nobts) {

            nobt.convert();

            final V12_1_1_NobtEntity nobtEntity = entityManager.find(V12_1_1_NobtEntity.class, nobt.getId());

            final Set<V12Person> convertedParticipants = nobt.getV13People();
            final Set<V12Person> participantsFromJPA = nobtEntity.getExplicitParticipants();

            assertThat(convertedParticipants.containsAll(participantsFromJPA), is(true));
            assertThat(participantsFromJPA.containsAll(convertedParticipants), is(true));
        }
    }
}
