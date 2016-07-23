package io.nobt.rest.docs;

import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.documentationConfiguration;

import javax.validation.Validation;
import javax.validation.Validator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.builder.RequestSpecBuilder;
import com.jayway.restassured.specification.RequestSpecification;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.springframework.restdocs.JUnitRestDocumentation;

import io.nobt.core.NobtCalculator;
import io.nobt.persistence.NobtDao;
import io.nobt.persistence.dao.InMemoryNobtDao;
import io.nobt.rest.NobtRestApi;
import io.nobt.rest.json.BodyParser;
import io.nobt.rest.json.ObjectMapperFactory;
import spark.Service;

public class AbstractApiTest {

    @Rule
    public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("build/generated-snippets");

    protected RequestSpecification documentationSpec;

    private Service http;
    protected NobtDao nobtDao;

    @Before
    public void setUp() throws Exception {

        this.documentationSpec = new RequestSpecBuilder()
                .addFilter(documentationConfiguration(restDocumentation)).build();

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
        ).run(8080);
    }

    @After
    public void tearDown() throws Exception {
        http.stop();
    }
}
