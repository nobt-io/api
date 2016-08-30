package io.nobt.sql.flyway;

import io.nobt.dbconfig.test.PortParameterizablePostgresDatabaseConfig;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import pl.domzal.junit.docker.rule.DockerRule;

public class MigrationServiceIT {

    private static final PortParameterizablePostgresDatabaseConfig config = new PortParameterizablePostgresDatabaseConfig(7654);

    @ClassRule
    public static DockerRule postgresRule = DockerRule
            .builder()
            .imageName("postgres:9")
            .expose(config.port().toString(), "5432")
            .env("POSTGRES_PASSWORD", config.password())
            .waitForMessage("PostgreSQL init process complete")
            .keepContainer(true)
            .build();

    private MigrationService sut;

    @Before
    public void setUp() throws Exception {
        sut = new MigrationService();
    }

    @Test
    public void shouldPerformMigrationsWithoutError() throws Exception {
        sut.migrateDatabaseAt(config);
    }
}