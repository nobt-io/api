package io.nobt.sql.flyway;

import io.nobt.dbconfig.test.ConfigurablePostgresTestDatabaseConfig;
import io.nobt.dbconfig.test.TestDatabaseConfig;
import io.nobt.test.persistence.DatabaseAvailabilityCheck;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.awaitility.Awaitility.await;

public class MigrationServiceIT {

    private static final TestDatabaseConfig databaseConfig = ConfigurablePostgresTestDatabaseConfig.parse(System::getenv);

    private MigrationService sut;

    @BeforeClass
    public static void waitForDatabase() {
        final DatabaseAvailabilityCheck availabilityCheck = new DatabaseAvailabilityCheck(databaseConfig);

        await().until(availabilityCheck::isDatabaseUp);
    }

    @Before
    public void setUp() throws Exception {
        sut = new MigrationService();
    }

    @Test
    public void shouldPerformMigrationsWithoutError() throws Exception {
        sut.migrateDatabaseAt(databaseConfig);
    }
}