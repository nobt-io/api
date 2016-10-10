package io.nobt.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.nobt.core.NobtCalculator;
import io.nobt.core.domain.NobtFactory;
import io.nobt.dbconfig.cloud.CloudDatabaseConfig;
import io.nobt.dbconfig.local.LocalDatabaseConfig;
import io.nobt.persistence.DatabaseConfig;
import io.nobt.persistence.NobtRepositoryCommandInvoker;
import io.nobt.profiles.Profile;
import io.nobt.rest.json.BodyParser;
import io.nobt.rest.json.ObjectMapperFactory;
import io.nobt.sql.flyway.MigrationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import spark.Service;

import javax.validation.Validation;
import javax.validation.Validator;

public class NobtApplication {

    private static final Logger LOGGER = LogManager.getLogger(NobtApplication.class);
    private static final int LOCAL_PORT = 8080;
    private static final Profile CURRENT_PROFILE = Profile.getCurrentProfile();

    public static void main(String[] args) {

        LOGGER.info("Running in profile {}.", CURRENT_PROFILE);

        final DatabaseConfig databaseConfig = getDatabaseConfig();

        if (databaseConfig != null) {
            migrateDatabase(databaseConfig);
        }

        final ObjectMapper objectMapper = new ObjectMapperFactory().create();
        final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

        final NobtRepositoryCommandInvoker invoker = CURRENT_PROFILE.getProfileDependentValue(
                NobtRepositoryCommandInvokerFactory::inMemory,
                () -> NobtRepositoryCommandInvokerFactory.transactional(databaseConfig),
                () -> NobtRepositoryCommandInvokerFactory.transactional(databaseConfig)
        );

        final int port = CURRENT_PROFILE.getProfileDependentValue(
                () -> LOCAL_PORT,
                () -> LOCAL_PORT,
                () -> Integer.parseInt(System.getenv("PORT"))
        );

        final NobtRestApi api = new NobtRestApi(
                Service.ignite(),
                invoker,
                new NobtCalculator(),
                new BodyParser(objectMapper, validator),
                objectMapper,
                new NobtFactory());

        api.run(port);
    }

    private static void migrateDatabase(DatabaseConfig databaseConfig) {
        final MigrationService migrationService = new MigrationService(databaseConfig);
        migrationService.migrate();
    }

    private static DatabaseConfig getDatabaseConfig() {
        return CURRENT_PROFILE.getProfileDependentValue(() -> null, LocalDatabaseConfig::create, CloudDatabaseConfig::create);
    }
}
