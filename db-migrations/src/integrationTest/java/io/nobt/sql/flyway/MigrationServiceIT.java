package io.nobt.sql.flyway;

import db.migration.v12.V12Person;
import io.nobt.persistence.DatabaseConfig;
import io.nobt.test.persistence.PostgreSQLContainerDatabaseConfig;
import io.nobt.util.Sets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.*;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class MigrationServiceIT {

    private static final Logger LOGGER = LogManager.getLogger();

    @ClassRule
    public static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:9.6");

    private static DatabaseConfig databaseConfig;

    private MigrationService sut;

    @BeforeClass
    public static void setupEnvironment() {

        databaseConfig = new PostgreSQLContainerDatabaseConfig(postgreSQLContainer);

        LOGGER.debug("Connecting to DB with url {}.", databaseConfig.url());
    }

    @Before
    public void setUp() throws Exception {
        sut = new MigrationService(databaseConfig);
    }

    @Test
    public void shouldPerformMigrationsWithoutError() throws Exception {
        sut.migrate();
    }

    @Test
    public void shouldCorrectlyMigrateExplicitParticipantsToJSONB() throws Exception {

        sut.migrate("12.0");

        try (final V12MigrationDao dao = new V12MigrationDao(databaseConfig)) {

            final Long firstId = dao.insertNobtWithParticipants("");
            final Long secondId = dao.insertNobtWithParticipants("Thomas;David;Lukas");

            sut.migrate("12.1");

            final Set<V12Person> participantsOfFirstNobt = dao.getParticipantsOfNobt(firstId);
            assertThat(participantsOfFirstNobt, hasSize(0));

            final Set<V12Person> participantsOfSecondNobt = dao.getParticipantsOfNobt(secondId);
            assertThat(participantsOfSecondNobt, is(
                    Sets.newHashSet(
                            V12Person.forName("Thomas"),
                            V12Person.forName("David"),
                            V12Person.forName("Lukas")
                    )
            ));
        }
    }

    @After
    public void tearDown() throws Exception {
        sut.clean();
    }
}