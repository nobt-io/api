package io.nobt.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.nobt.application.BodyParser;
import io.nobt.application.NobtRepositoryCommandInvokerFactory;
import io.nobt.application.ObjectMapperFactory;
import io.nobt.application.env.Config;
import io.nobt.application.env.ConfigBuilder;
import io.nobt.application.env.RealEnvironment;
import io.nobt.core.domain.NobtFactory;
import io.nobt.persistence.NobtRepositoryCommandInvoker;
import io.nobt.sql.flyway.MigrationService;
import io.nobt.test.persistence.PostgreSQLContainerDatabaseConfig;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.testcontainers.containers.PostgreSQLContainer;
import spark.Service;

import javax.validation.Validation;
import javax.validation.Validator;
import java.io.IOException;

public abstract class ApiIntegrationTestBase {

    @ClassRule
    public static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:9.6");

    private static MigrationService migrationService;
    private static Service http;

    protected static Config config;

    @BeforeClass
    public static void setupEnvironment() {

        config = ConfigBuilder
                .newInstance()
                .applyEnvironment(new RealEnvironment())
                .overridePort(8080)
                .overrideDatabase(new PostgreSQLContainerDatabaseConfig(postgreSQLContainer))
                .build();

        migrationService = new MigrationService(config.database());
        migrationService.migrate();

        final ObjectMapper objectMapper = new ObjectMapperFactory().create();
        final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        final NobtRepositoryCommandInvoker nobtRepositoryCommandInvoker = new NobtRepositoryCommandInvokerFactory(config).create();

        http = Service.ignite();

        new NobtRestApi(
                http,
                nobtRepositoryCommandInvoker,
                new BodyParser(objectMapper, validator),
                objectMapper,
                new NobtFactory()
        ).run(config.port());
    }

    @AfterClass
    public static void cleanupEnvironment() throws IOException {
        http.stop();
        migrationService.clean();
    }
}
