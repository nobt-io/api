package io.nobt.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.nobt.core.NobtCalculator;
import io.nobt.dbconfig.test.ConfigurablePostgresTestDatabaseConfig;
import io.nobt.persistence.DatabaseConfig;
import io.nobt.persistence.EntityManagerFactoryProvider;
import io.nobt.persistence.NobtRepository;
import io.nobt.persistence.NobtRepositoryImpl;
import io.nobt.persistence.mapping.ExpenseMapper;
import io.nobt.persistence.mapping.NobtMapper;
import io.nobt.persistence.mapping.ShareMapper;
import io.nobt.rest.json.BodyParser;
import io.nobt.rest.json.ObjectMapperFactory;
import io.nobt.sql.flyway.MigrationService;
import io.nobt.test.persistence.DatabaseAvailabilityCheck;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import spark.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.validation.Validation;
import javax.validation.Validator;

import static org.awaitility.Awaitility.await;

public abstract class ApiIntegrationTestBase {

    protected static final int ACTUAL_PORT = 18080;

    private static DatabaseConfig databaseConfig;
    private static MigrationService migrationService;
    private static Service http;
    private static EntityManager entityManager;
    private static EntityManagerFactory entityManagerFactory;

    protected static NobtRepository nobtRepository;

    @BeforeClass
    public static void setupEnvironment() {

        databaseConfig = ConfigurablePostgresTestDatabaseConfig.parse(System::getenv);
        migrationService = new MigrationService(databaseConfig);

        final DatabaseAvailabilityCheck availabilityCheck = new DatabaseAvailabilityCheck(databaseConfig);
        await().until(availabilityCheck::isDatabaseUp);

        migrationService.migrate();

        final ObjectMapper objectMapper = new ObjectMapperFactory().create();
        final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

        final EntityManagerFactoryProvider emfProvider = new EntityManagerFactoryProvider();
        entityManagerFactory = emfProvider.create(databaseConfig);
        entityManager = entityManagerFactory.createEntityManager();

        final ShareMapper shareMapper = new ShareMapper();
        final ExpenseMapper expenseMapper = new ExpenseMapper(shareMapper);
        final NobtMapper nobtMapper = new NobtMapper(expenseMapper);

        nobtRepository = new NobtRepositoryImpl(
                entityManager,
                nobtMapper
        );

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
    public static void cleanupEnvironment() {
        http.stop();

        entityManager.close();
        entityManagerFactory.close();

        migrationService.clean();
    }
}
