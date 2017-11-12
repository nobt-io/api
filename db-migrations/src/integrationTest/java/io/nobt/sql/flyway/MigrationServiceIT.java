package io.nobt.sql.flyway;

import db.migration.v12.V12Person;
import io.nobt.persistence.DatabaseConfig;
import io.nobt.test.persistence.PostgreSQLContainerDatabaseConfig;
import io.nobt.util.Sets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.*;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.hamcrest.Matchers.*;
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

        sut.setTargetVersion("12.1");
        sut.migrate();

        try (final V12MigrationDao dao = new V12MigrationDao(databaseConfig)) {

            assertThat("We want at least 3 instances after the migration", dao.getAllNobts(), hasSize(anyOf(
                    greaterThan(3),
                    equalTo(3)
            )));

            assertThat("Each participant should be an item of the set", dao.getParticipantsOfNobt(2), hasSize(5));

            assertThat("Name should now be present in the set", dao.getParticipantsOfNobt(3), is(Sets.newHashSet(V12Person.forName("Anna"))));

            assertThat("No participants should result in empty set", dao.getParticipantsOfNobt(4), is(empty()));
        }
    }

    @After
    public void tearDown() throws Exception {
        sut.clean();
    }
}