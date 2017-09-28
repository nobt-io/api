package io.nobt.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.nobt.application.BodyParser;
import io.nobt.application.NobtRepositoryCommandInvokerFactory;
import io.nobt.application.ObjectMapperFactory;
import io.nobt.application.env.Config;
import io.nobt.application.env.RealEnvironment;
import io.nobt.core.domain.NobtFactory;
import io.nobt.persistence.DatabaseConfig;
import io.nobt.persistence.NobtRepositoryCommandInvoker;
import io.nobt.sql.flyway.MigrationService;
import io.nobt.test.persistence.DatabaseAvailabilityCheck;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import spark.Service;

import javax.validation.Validation;
import javax.validation.Validator;
import java.io.IOException;

import static org.awaitility.Awaitility.await;

public abstract class ApiIntegrationTestBase {

    private static MigrationService migrationService;
    private static Service http;

    protected static Config config;

    @BeforeClass
    public static void setupEnvironment() {

        config = Config.from(new RealEnvironment());

        DatabaseConfig databaseConfig = config.database();

        migrationService = new MigrationService(databaseConfig);

        final DatabaseAvailabilityCheck availabilityCheck = new DatabaseAvailabilityCheck(databaseConfig);
        await().until(availabilityCheck::isDatabaseUp);

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
