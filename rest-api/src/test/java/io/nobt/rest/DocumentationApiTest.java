package io.nobt.rest;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.restassured.operation.preprocess.RestAssuredPreprocessors.modifyUris;

import java.math.BigDecimal;
import java.util.Set;

import javax.validation.Validation;
import javax.validation.Validator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.builder.RequestSpecBuilder;
import com.jayway.restassured.specification.RequestSpecification;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.restdocs.JUnitRestDocumentation;

import io.nobt.core.NobtCalculator;
import io.nobt.core.domain.Nobt;
import io.nobt.core.domain.Person;
import io.nobt.persistence.NobtDao;
import io.nobt.persistence.dao.InMemoryNobtDao;
import io.nobt.rest.NobtRestApi;
import io.nobt.rest.json.BodyParser;
import io.nobt.rest.json.ObjectMapperFactory;
import io.nobt.util.Sets;
import spark.Service;

public class DocumentationApiTest {

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

    @Test
    public void shouldCreateNewNobt() throws Exception {

        given(this.documentationSpec).
                filter(
                        document("create-nobt",
                                preprocessRequest(modifyUris().scheme("http").host("localhost").port(8080)),
                                requestFields(
                                        fieldWithPath("nobtName").description("The name of the nobt."),
                                        fieldWithPath("explicitParticipants").optional().description("An array of people that should always be listed as participants, no matter if they ever participate as debtee / debtor or not.")
                                )
                        )
                ).
                body("{\n" +
                        "  \"nobtName\": \"Grillfeier\",\n" +
                        "  \"explicitParticipants\": [\n" +
                        "    \"Thomas\",\n" +
                        "    \"Martin\",\n" +
                        "    \"Lukas\"\n" +
                        "  ]\n" +
                        "}").
                contentType("application/json").
        when().
                post("/nobts").
        then().
                statusCode(201).
                header("Location", not(isEmptyOrNullString()));
    }

    @Test
    public void shouldAddNewExpense() throws Exception {

        final Set<Person> explicitParticipants = Sets.newHashSet(Person.forName("Thomas"), Person.forName("Martin"), Person.forName("Lukas"));
        final Nobt nobt = nobtDao.create("Grillfeier", explicitParticipants);

        given(this.documentationSpec)
                .filter(
                        document("create-expense",
                                preprocessRequest(modifyUris().scheme("http").host("localhost").port(8080)),
                                requestFields(
                                        fieldWithPath("name").description("Human readable description of the expense"),
                                        fieldWithPath("amount").description("The amount of the expense in EUR."),
                                        fieldWithPath("debtee").description("The name of the person who made the expense"),
                                        fieldWithPath("debtors").description("The names of the persons who take part in this expense or profit from it. Note that the debtee in this example is also included. This is usually the default if you want to split a bill among three people. For the unlikely scenario that in this case Thomas made the expense but the expense should be split up only between Martin and Lukas, Thomas should be omitted from the debtors array.")
                                )
                        )
                )
                .body("{\n" +
                        "  \"name\": \"Fleisch\",\n" +
                        "  \"amount\": 12.99,\n" +
                        "  \"debtee\": \"Thomas\",\n" +
                        "  \"debtors\": [\n" +
                        "    \"Thomas\",\n" +
                        "    \"Martin\",\n" +
                        "    \"Lukas\"\n" +
                        "  ]\n" +
                        "}")
                .contentType("application/json")
        .when()
                .post("/nobts/" + nobt.getId().toExternalIdentifier() + "/expenses")
        .then()
                .statusCode(201);
    }

    @Test
    public void shouldCreateNewExpense() throws Exception {

        final Set<Person> explicitParticipants = Sets.newHashSet(Person.forName("Thomas"), Person.forName("Martin"), Person.forName("Lukas"));
        final Nobt nobt = nobtDao.create("Grillfeier", explicitParticipants);
        nobtDao.createExpense(nobt.getId(), "Fleisch", new BigDecimal(12.99), Person.forName("Thomas"), explicitParticipants);

        given(this.documentationSpec)
                .filter(
                        document("get-nobt",
                                preprocessRequest(modifyUris().scheme("http").host("localhost").port(8080)),
                                responseFields(
                                        fieldWithPath("id").description("The id of the nobt. Can be used to construct URIs to the various endpoints of the API"),
                                        fieldWithPath("name").description("The name of the nobt."),
                                        fieldWithPath("expenses").description("All expenses associated with this nobt."),
                                        fieldWithPath("participatingPersons").description("An array of persons participating in this nobt. Contains the explicit participants pass to the API on creation of the nobt and all persons that take part in this nobt either as debtee or as debtor. Each name is only contained once."),
                                        fieldWithPath("transactions").description("Contains an array of transactions that need to be made so that all debts are paid.")
                                )
                        )
                )
        .when()
                .get("/nobts/" + nobt.getId().toExternalIdentifier())
        .then()
                .statusCode(200);
    }
}
