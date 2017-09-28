package io.nobt.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.nobt.application.env.Config;
import io.nobt.core.domain.NobtFactory;
import io.nobt.persistence.DatabaseConfig;
import io.nobt.persistence.NobtRepositoryCommandInvoker;
import io.nobt.rest.NobtRestApi;
import io.nobt.sql.flyway.MigrationService;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import spark.Service;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import static io.nobt.application.env.Config.Keys.DATABASE_CONNECTION_STRING;
import static io.nobt.application.env.Config.Keys.PORT;
import static io.nobt.application.env.MissingConfigurationException.missingConfigurationException;

public class NobtApplication {

    public static final Marker SENTRY = MarkerManager.getMarker("SENTRY");

    private final NobtRepositoryCommandInvokerFactory nobtRepositoryCommandInvokerFactory;
    private final ObjectMapperFactory objectMapperFactory;
    private final ValidatorFactory validatorFactory;

    public NobtApplication(NobtRepositoryCommandInvokerFactory nobtRepositoryCommandInvokerFactory, ObjectMapperFactory objectMapperFactory, ValidatorFactory validatorFactory) {
        this.nobtRepositoryCommandInvokerFactory = nobtRepositoryCommandInvokerFactory;
        this.objectMapperFactory = objectMapperFactory;
        this.validatorFactory = validatorFactory;
    }

    public static void main(String[] args) {

        final NobtApplication application = new NobtApplication(
                new NobtRepositoryCommandInvokerFactory(),
                new ObjectMapperFactory(),
                Validation.buildDefaultValidatorFactory()
        );

        application.start();
    }

    public void start() {

        if (Config.migrateDatabaseAtStartUp()) {
            migrateDatabase();
        }

        final NobtRepositoryCommandInvoker nobtRepositoryCommandInvoker = nobtRepositoryCommandInvokerFactory.create();
        final ObjectMapper objectMapper = objectMapperFactory.create();
        final Validator validator = validatorFactory.getValidator();

        final NobtRestApi api = new NobtRestApi(
                Service.ignite(),
                nobtRepositoryCommandInvoker,
                new BodyParser(objectMapper, validator),
                objectMapper,
                new NobtFactory()
        );

        final int httpPort = Config.port().orElseThrow(missingConfigurationException(PORT));

        api.run(httpPort);
    }

    private void migrateDatabase() {
        final DatabaseConfig databaseConfig = Config.database();

        final MigrationService migrationService = new MigrationService(databaseConfig);
        migrationService.migrate();
    }
}
