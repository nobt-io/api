package io.nobt.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.nobt.core.NobtCalculator;
import io.nobt.core.domain.NobtFactory;
import io.nobt.dbconfig.test.ConfigurablePostgresTestDatabaseConfig;
import io.nobt.persistence.DatabaseConfig;
import io.nobt.rest.json.BodyParser;
import io.nobt.rest.json.ObjectMapperFactory;
import io.nobt.sql.flyway.MigrationService;
import io.nobt.test.persistence.DatabaseAvailabilityCheck;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import spark.Service;

import javax.validation.Validation;
import javax.validation.Validator;

import static org.awaitility.Awaitility.await;

public abstract class ApiIntegrationTestBase {

    protected static final int ACTUAL_PORT = 18080;

    private static MigrationService migrationService;
    private static Service http;

    @BeforeClass
    public static void setupEnvironment() {

        DatabaseConfig databaseConfig = ConfigurablePostgresTestDatabaseConfig.parse(System::getenv);
        migrationService = new MigrationService(databaseConfig);

        final DatabaseAvailabilityCheck availabilityCheck = new DatabaseAvailabilityCheck(databaseConfig);
        await().until(availabilityCheck::isDatabaseUp);

        migrationService.migrate();

        final ObjectMapper objectMapper = new ObjectMapperFactory().create();
        final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

        http = Service.ignite();

        new NobtRestApi(
                http,
                NobtRepositoryCommandInvokerFactory.transactional(databaseConfig),
                new NobtCalculator(),
                new BodyParser(objectMapper, validator),
                objectMapper,
                new NobtFactory()
        ).run(ACTUAL_PORT);
    }

    @AfterClass
    public static void cleanupEnvironment() {
        http.stop();
        migrationService.clean();
    }
}
