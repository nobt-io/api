package io.nobt.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.nobt.core.NobtCalculator;
import io.nobt.dbconfig.CloudDatabaseConfig;
import io.nobt.dbconfig.DatabaseConfig;
import io.nobt.dbconfig.LocalDatabaseConfig;
import io.nobt.persistence.NobtDao;
import io.nobt.persistence.dao.InMemoryNobtDao;
import io.nobt.persistence.dao.NobtDaoImpl;
import io.nobt.persistence.dao.NobtMapper;
import io.nobt.profiles.Profile;
import io.nobt.rest.handler.ConstraintViolationExceptionHandler;
import io.nobt.rest.json.BodyParser;
import io.nobt.rest.json.JacksonResponseTransformer;
import io.nobt.rest.json.ObjectMapperFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import spark.Service;

import javax.persistence.EntityManager;
import javax.validation.Validation;
import javax.validation.Validator;

public class NobtApplication {

    private static final Logger LOGGER = LogManager.getLogger(NobtApplication.class);
    private static final int LOCAL_PORT = 8080;
    private static final Profile CURRENT_PROFILE = Profile.getCurrentProfile();

    public static void main(String[] args) {

        LOGGER.info("Running in profile {}.", CURRENT_PROFILE);

        final ObjectMapper objectMapper = new ObjectMapperFactory().create();
        final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

        final NobtDao nobtDao = CURRENT_PROFILE.getProfileDependentValue(
                InMemoryNobtDao::new,
                NobtApplication::createSqlBackedDao,
                NobtApplication::createSqlBackedDao
        );

        final int port = CURRENT_PROFILE.getProfileDependentValue(
                () -> LOCAL_PORT,
                () -> LOCAL_PORT,
                () -> Integer.parseInt(System.getenv("PORT"))
        );

        new NobtRestApi(
                Service.ignite(),
                nobtDao,
                new NobtCalculator(),
                new BodyParser(objectMapper, validator),
                new JacksonResponseTransformer(objectMapper),
                new ConstraintViolationExceptionHandler(objectMapper)
        ).run(port);
    }

    private static NobtDao createSqlBackedDao() {
        final DatabaseConfig databaseConfig = CURRENT_PROFILE.getProfileDependentValue(() -> null, LocalDatabaseConfig::create, CloudDatabaseConfig::create);
        final EntityManager entityManager = new EntityManagerFactoryProvider().create(databaseConfig).createEntityManager();

        return new NobtDaoImpl(entityManager, new NobtMapper());
    }
}
