package io.nobt.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.nobt.core.NobtCalculator;
import io.nobt.core.domain.Expense;
import io.nobt.core.domain.Share;
import io.nobt.dbconfig.cloud.CloudDatabaseConfig;
import io.nobt.dbconfig.local.LocalDatabaseConfig;
import io.nobt.persistence.*;
import io.nobt.persistence.entity.ExpenseEntity;
import io.nobt.persistence.entity.ShareEntity;
import io.nobt.persistence.mapping.DomainModelMapper;
import io.nobt.persistence.mapping.ExpenseMapper;
import io.nobt.persistence.mapping.NobtMapper;
import io.nobt.persistence.mapping.ShareMapper;
import io.nobt.profiles.Profile;
import io.nobt.rest.json.BodyParser;
import io.nobt.rest.json.ObjectMapperFactory;
import io.nobt.sql.flyway.MigrationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import spark.Service;

import javax.persistence.EntityManager;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Optional;

public class NobtApplication {

    private static final Logger LOGGER = LogManager.getLogger(NobtApplication.class);
    private static final int LOCAL_PORT = 8080;
    private static final Profile CURRENT_PROFILE = Profile.getCurrentProfile();

    public static void main(String[] args) {

        LOGGER.info("Running in profile {}.", CURRENT_PROFILE);

        final DatabaseConfig databaseConfig = getDatabaseConfig();

        Optional.ofNullable(databaseConfig).ifPresent(NobtApplication::migrateDatabase);

        final ObjectMapper objectMapper = new ObjectMapperFactory().create();
        final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

        final NobtRepository nobtRepository = CURRENT_PROFILE.getProfileDependentValue(
                InMemoryNobtRepository::new,
                NobtApplication::createSqlBackedDao,
                NobtApplication::createSqlBackedDao
        );

        final int port = CURRENT_PROFILE.getProfileDependentValue(
                () -> LOCAL_PORT,
                () -> LOCAL_PORT,
                () -> Integer.parseInt(System.getenv("PORT"))
        );

        final NobtRestApi api = new NobtRestApi(
                Service.ignite(),
                nobtRepository,
                new NobtCalculator(),
                new BodyParser(objectMapper, validator),
                objectMapper
        );

        api.run(port);
    }

    private static void migrateDatabase(DatabaseConfig databaseConfig) {
        final MigrationService migrationService = new MigrationService(databaseConfig);
        migrationService.migrate();
    }

    private static NobtRepository createSqlBackedDao() {
        final DatabaseConfig databaseConfig = getDatabaseConfig();
        final EntityManager entityManager = new EntityManagerFactoryProvider().create(databaseConfig).createEntityManager();

        final DomainModelMapper<ShareEntity, Share> shareMapper = new ShareMapper();
        final DomainModelMapper<ExpenseEntity, Expense> expenseMapper = new ExpenseMapper(shareMapper);

        return new NobtRepositoryImpl(entityManager, new NobtMapper(expenseMapper));
    }

    private static DatabaseConfig getDatabaseConfig() {
        return CURRENT_PROFILE.getProfileDependentValue(() -> null, LocalDatabaseConfig::create, CloudDatabaseConfig::create);
    }
}
