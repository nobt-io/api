package io.nobt.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.nobt.application.env.Config;
import io.nobt.application.env.ConfigBuilder;
import io.nobt.application.env.RealEnvironment;
import io.nobt.core.domain.NobtFactory;
import io.nobt.persistence.DatabaseConfig;
import io.nobt.persistence.NobtRepositoryCommandInvoker;
import io.nobt.rest.NobtRestApi;
import io.nobt.sql.flyway.MigrationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import spark.Service;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.Closeable;
import java.io.IOException;

public class NobtApplication implements Closeable {

    private static final Logger LOGGER = LogManager.getLogger();

    public static final Marker SENTRY = MarkerManager.getMarker("SENTRY");

    private final NobtRepositoryCommandInvokerFactory nobtRepositoryCommandInvokerFactory;
    private final ObjectMapperFactory objectMapperFactory;
    private final ValidatorFactory validatorFactory;
    private final Config config;
    private NobtRestApi api;

    public NobtApplication(Config config) {
        this.nobtRepositoryCommandInvokerFactory = new NobtRepositoryCommandInvokerFactory(config);
        this.objectMapperFactory = new ObjectMapperFactory();
        this.validatorFactory = Validation.buildDefaultValidatorFactory();
        this.config = config;
    }

    public static void main(String[] args) {

        final Config config = ConfigBuilder
                .newInstance()
                .applyEnvironment(new RealEnvironment())
                .build();

        final NobtApplication application = new NobtApplication(config);

        application.start();
    }

    public void start() {

        if (config.database() != null && !config.migrateDatabaseAtStartUp()) {
            LOGGER.warn(SENTRY, "Application about to connect to real database without migrating first.");
        }

        if (config.migrateDatabaseAtStartUp()) {
            migrateDatabase();
        }

        final NobtRepositoryCommandInvoker nobtRepositoryCommandInvoker = nobtRepositoryCommandInvokerFactory.create();
        final ObjectMapper objectMapper = objectMapperFactory.create();
        final Validator validator = validatorFactory.getValidator();

        api = new NobtRestApi(
                Service.ignite(),
                nobtRepositoryCommandInvoker,
                new BodyParser(objectMapper, validator),
                objectMapper,
                new NobtFactory()
        );

        final int httpPort = config.port();

        api.run(httpPort, config.schemeOverrideHeader());
    }

    private void migrateDatabase() {
        final DatabaseConfig databaseConfig = config.database();

        final MigrationService migrationService = new MigrationService(databaseConfig);
        migrationService.migrate();
    }

    @Override
    public void close() throws IOException {
        api.close();
    }
}
