package io.nobt.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.nobt.core.NobtCalculator;
import io.nobt.persistence.NobtDao;
import io.nobt.persistence.dao.InMemoryNobtDao;
import io.nobt.rest.json.BodyParser;
import io.nobt.rest.json.ObjectMapperFactory;
import org.junit.After;
import org.junit.Before;
import spark.Service;

import javax.validation.Validation;
import javax.validation.Validator;

public abstract class ApiIntegrationTestBase {

    protected static final int ACTUAL_PORT = 18080;

    private Service http;
    protected NobtDao nobtDao;

    @Before
    public void startAPI() throws Exception {

        final ObjectMapper objectMapper = new ObjectMapperFactory().create();
        final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        nobtDao = new InMemoryNobtDao();

        http = Service.ignite();

        new NobtRestApi(
                http,
                nobtDao,
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
