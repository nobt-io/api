package io.nobt.sql.flyway;

import io.nobt.dbconfig.test.ConfigurablePostgresTestDatabaseConfig;
import io.nobt.dbconfig.test.TestDatabaseConfig;
import org.junit.Before;
import org.junit.Test;

public class MigrationServiceIT {

    private static final TestDatabaseConfig databaseConfig = ConfigurablePostgresTestDatabaseConfig.parse(System::getenv);

    private MigrationService sut;

    @Before
    public void setUp() throws Exception {
        sut = new MigrationService();
    }

    @Test
    public void shouldPerformMigrationsWithoutError() throws Exception {
        sut.migrateDatabaseAt(databaseConfig);
    }
}