package io.nobt.sql.flyway;

import io.nobt.application.env.Config;
import io.nobt.application.env.RealEnvironment;
import io.nobt.persistence.DatabaseConfig;
import io.nobt.test.persistence.DatabaseAvailabilityCheck;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.awaitility.Awaitility.await;

public class MigrationServiceIT {

    private static DatabaseConfig databaseConfig;

    private MigrationService sut;

    @BeforeClass
    public static void setupEnvironment() {

        final Config config = Config.from(new RealEnvironment());

        databaseConfig = config.database();

        final DatabaseAvailabilityCheck availabilityCheck = new DatabaseAvailabilityCheck(databaseConfig);
        await().until(availabilityCheck::isDatabaseUp);
    }

    @Before
    public void setUp() throws Exception {
        sut = new MigrationService(databaseConfig);
    }

    @Test
    public void shouldPerformMigrationsWithoutError() throws Exception {
        sut.migrate();
    }

    @After
    public void tearDown() throws Exception {
        sut.clean();
    }
}