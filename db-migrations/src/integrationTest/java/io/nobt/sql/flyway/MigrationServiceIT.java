package io.nobt.sql.flyway;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import pl.domzal.junit.docker.rule.DockerRule;

public class MigrationServiceIT {

    @ClassRule
    public static DockerRule postgresRule = DockerRule
            .builder()
            .imageName("postgres:9")
            .expose("5432", "5432")
            .env("POSTGRES_PASSWORD", "password")
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

        final String url = "jdbc:postgresql://localhost:5432/postgres";
        final String username = "postgres";
        final String password = "password";

        sut.migrateDatabaseAt(url, username, password);
    }
}