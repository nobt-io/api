package io.nobt.test;

import io.nobt.dbconfig.test.TestDatabaseConfig;
import org.junit.rules.ExternalResource;
import pl.domzal.junit.docker.rule.DockerRule;

public class PostgresDockerRule extends ExternalResource {

    private final DockerRule delegate;

    public PostgresDockerRule(DockerRule delegate) {
        this.delegate = delegate;
    }

    public static PostgresDockerRule forDatabase(TestDatabaseConfig databaseConfig) {
        final DockerRule postgresDockerRule = DockerRule
                .builder()
                .imageName("postgres:9")
                .expose(databaseConfig.port().toString(), "5432")
                .env("POSTGRES_PASSWORD", databaseConfig.password())
                .waitForMessage("PostgreSQL init process complete; ready for start up.", 60)
                .keepContainer(false)
                .build();

        return new PostgresDockerRule(postgresDockerRule);
    }

    @Override
    protected void before() throws Throwable {
        delegate.before();
    }

    @Override
    protected void after() {
        delegate.after();
    }
}
