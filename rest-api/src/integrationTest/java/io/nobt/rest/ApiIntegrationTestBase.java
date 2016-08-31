package io.nobt.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.nobt.core.NobtCalculator;
import io.nobt.dbconfig.test.PortParameterizablePostgresDatabaseConfig;
import io.nobt.persistence.EntityManagerFactoryProvider;
import io.nobt.persistence.NobtRepository;
import io.nobt.persistence.NobtRepositoryImpl;
import io.nobt.persistence.mapping.ExpenseMapper;
import io.nobt.persistence.mapping.NobtMapper;
import io.nobt.persistence.mapping.ShareMapper;
import io.nobt.rest.json.BodyParser;
import io.nobt.rest.json.ObjectMapperFactory;
import io.nobt.sql.flyway.MigrationService;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import pl.domzal.junit.docker.rule.DockerRule;
import spark.Service;

import javax.persistence.EntityManagerFactory;
import javax.validation.Validation;
import javax.validation.Validator;

// TODO using more than one subclass of this is not possible because the port is hard coded here and that makes parallel execution impossible
public abstract class ApiIntegrationTestBase {

    protected static final int ACTUAL_PORT = 18080;
    protected static final PortParameterizablePostgresDatabaseConfig databaseConfig = new PortParameterizablePostgresDatabaseConfig(6543);

    private Service http;
    protected NobtRepository nobtRepository;

    @ClassRule
    public static DockerRule postgresRule = DockerRule
            .builder()
            .imageName("postgres:9")
            .expose(databaseConfig.port().toString(), "5432")
            .env("POSTGRES_PASSWORD", databaseConfig.password())
            .waitForMessage("PostgreSQL init process complete")
            .keepContainer(false)
            .build();

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
