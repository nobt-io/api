package io.nobt.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.nobt.core.NobtCalculator;
import io.nobt.dbconfig.test.ConfigurablePostgresTestDatabaseConfig;
import io.nobt.dbconfig.test.TestDatabaseConfig;
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
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import spark.Service;

import javax.persistence.EntityManagerFactory;
import javax.validation.Validation;
import javax.validation.Validator;

import static org.awaitility.Awaitility.await;

public abstract class ApiIntegrationTestBase {

    protected static final int ACTUAL_PORT = 18080;
    protected static final TestDatabaseConfig databaseConfig = ConfigurablePostgresTestDatabaseConfig.parse(System::getenv);

    private Service http;
    protected NobtRepository nobtRepository;

    @BeforeClass
    public static void waitForDatabase() {
        final DatabaseAvailabilityCheck availabilityCheck = new DatabaseAvailabilityCheck(databaseConfig);

        await().until(availabilityCheck::isDatabaseUp);
    }

    @Before
    public void startAPI() throws Exception {

        final ObjectMapper objectMapper = new ObjectMapperFactory().create();
        final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

        new MigrationService().migrateDatabaseAt(databaseConfig);

        final EntityManagerFactoryProvider emfProvider = new EntityManagerFactoryProvider();
        final EntityManagerFactory entityManagerFactory = emfProvider.create(databaseConfig);

        final ShareMapper shareMapper = new ShareMapper();
        final ExpenseMapper expenseMapper = new ExpenseMapper(shareMapper);
        final NobtMapper nobtMapper = new NobtMapper(expenseMapper);

        nobtRepository = new NobtRepositoryImpl(
                entityManagerFactory.createEntityManager(),
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

    @After
    public void stopAPI() throws Exception {
        http.stop();
    }
}
