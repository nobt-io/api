package io.nobt.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.nobt.application.env.Config;
import io.nobt.core.NobtCalculator;
import io.nobt.persistence.DatabaseConfig;
import io.nobt.persistence.NobtRepository;
import io.nobt.rest.NobtRestApi;
import io.nobt.sql.flyway.MigrationService;
import spark.Service;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import static io.nobt.application.env.Config.Keys.DATABASE_CONNECTION_STRING;
import static io.nobt.application.env.Config.Keys.PORT;
import static io.nobt.application.env.MissingConfigurationException.missingConfigurationException;

public class NobtApplication {

    private final NobtRepositoryFactory nobtRepositoryFactory;
    private final ObjectMapperFactory objectMapperFactory;
    private final ValidatorFactory validatorFactory;

    public NobtApplication(NobtRepositoryFactory nobtRepositoryFactory, ObjectMapperFactory objectMapperFactory, ValidatorFactory validatorFactory) {
        this.nobtRepositoryFactory = nobtRepositoryFactory;
        this.objectMapperFactory = objectMapperFactory;
        this.validatorFactory = validatorFactory;
    }

    public static void main(String[] args) {

        final NobtApplication application = new NobtApplication(
                new NobtRepositoryFactory(),
                new ObjectMapperFactory(),
                Validation.buildDefaultValidatorFactory()
        );

        application.start();
    }

    public void start() {

        if (Config.migrateDatabaseAtStartUp().orElse(false)) {
            migrateDatabase();
        }

        final NobtRepository nobtRepository = nobtRepositoryFactory.create();
        final ObjectMapper objectMapper = objectMapperFactory.create();
        final Validator validator = validatorFactory.getValidator();

        final NobtRestApi api = new NobtRestApi(
                Service.ignite(),
                nobtRepository,
                new NobtCalculator(),
                new BodyParser(objectMapper, validator),
                objectMapper
        );

        final int httpPort = Config.port().orElseThrow( missingConfigurationException(PORT) );

        api.run(httpPort);
    }

    private void migrateDatabase() {
        final DatabaseConfig databaseConfig = Config.database().orElseThrow( missingConfigurationException(DATABASE_CONNECTION_STRING) );

        final MigrationService migrationService = new MigrationService(databaseConfig);
        migrationService.migrate();
    }
}
