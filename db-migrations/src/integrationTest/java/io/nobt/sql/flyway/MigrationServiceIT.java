package io.nobt.sql.flyway;

import io.nobt.dbconfig.test.PortParameterizablePostgresDatabaseConfig;
import io.nobt.dbconfig.test.TestDatabaseConfig;
import io.nobt.test.PostgresDockerRule;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import pl.domzal.junit.docker.rule.DockerRule;

public class MigrationServiceIT {

    private static final TestDatabaseConfig databaseConfig = new PortParameterizablePostgresDatabaseConfig(7654);

    @ClassRule
    public static PostgresDockerRule postgresDockerRule = PostgresDockerRule.forDatabase(databaseConfig);

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