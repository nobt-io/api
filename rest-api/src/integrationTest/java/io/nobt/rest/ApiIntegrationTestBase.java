package io.nobt.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.nobt.application.BodyParser;
import io.nobt.application.NobtRepositoryFactory;
import io.nobt.application.ObjectMapperFactory;
import io.nobt.application.env.Config;
import io.nobt.core.NobtCalculator;
import io.nobt.persistence.DatabaseConfig;
import io.nobt.persistence.NobtRepository;
import io.nobt.persistence.NobtRepositoryImpl;
import io.nobt.sql.flyway.MigrationService;
import io.nobt.test.persistence.DatabaseAvailabilityCheck;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import spark.Service;

import javax.validation.Validation;
import javax.validation.Validator;
import java.io.IOException;

import static io.nobt.application.env.Config.Keys.DATABASE_CONNECTION_STRING;
import static io.nobt.application.env.MissingConfigurationException.missingConfigurationException;
import static org.awaitility.Awaitility.await;

public abstract class ApiIntegrationTestBase {

    protected static final int ACTUAL_PORT = 18080;

    private static MigrationService migrationService;
    private static Service http;

    protected static NobtRepository nobtRepository;

    @BeforeClass
    public static void setupEnvironment() {

        DatabaseConfig databaseConfig = Config.database().orElseThrow(missingConfigurationException(DATABASE_CONNECTION_STRING));

        migrationService = new MigrationService(databaseConfig);

        final DatabaseAvailabilityCheck availabilityCheck = new DatabaseAvailabilityCheck(databaseConfig);
        await().until(availabilityCheck::isDatabaseUp);

        migrationService.migrate();

        final ObjectMapper objectMapper = new ObjectMapperFactory().create();
        final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

        nobtRepository = new NobtRepositoryFactory().create();

        http = Service.ignite();

        new NobtRestApi(
                http,
                nobtRepository,
                new NobtCalculator(),
                new BodyParser(objectMapper, validator),
                objectMapper
        ).run(ACTUAL_PORT);
    }

    @AfterClass
    public static void cleanupEnvironment() throws IOException {
        http.stop();

        ((NobtRepositoryImpl) nobtRepository).close();

        migrationService.clean();
    }
}
