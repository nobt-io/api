package io.nobt.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.builder.RequestSpecBuilder;
import com.jayway.restassured.specification.RequestSpecification;
import io.nobt.application.BodyParser;
import io.nobt.application.NobtRepositoryCommandInvokerFactory;
import io.nobt.application.ObjectMapperFactory;
import io.nobt.application.env.Config;
import io.nobt.application.env.ConfigBuilder;
import io.nobt.application.env.RealEnvironment;
import io.nobt.core.domain.NobtFactory;
import io.nobt.persistence.NobtRepositoryCommandInvoker;
import io.nobt.sql.flyway.MigrationService;
import io.nobt.test.persistence.PostgreSQLContainerDatabaseConfig;
import org.junit.*;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.payload.JsonFieldType;
import org.testcontainers.containers.PostgreSQLContainer;
import spark.Service;

import javax.validation.Validation;
import javax.validation.Validator;
import java.io.IOException;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.restassured.operation.preprocess.RestAssuredPreprocessors.modifyUris;

public class ApiDocumentationTest {

    private static final int DOCUMENTED_PORT = 80;

    @Rule
    public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("build/generated-snippets");


    @ClassRule
    public static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:9.6");

    private static MigrationService migrationService;
    private static Service http;

    protected static Config config;

    @BeforeClass
    public static void setupEnvironment() {

        config = ConfigBuilder
                .newInstance()
                .applyEnvironment(new RealEnvironment())
                .overridePort(8080)
                .overrideDatabase(new PostgreSQLContainerDatabaseConfig(postgreSQLContainer))
                .build();

        migrationService = new MigrationService(config.database());
        migrationService.migrate();

        final ObjectMapper objectMapper = new ObjectMapperFactory().create();
        final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        final NobtRepositoryCommandInvoker nobtRepositoryCommandInvoker = new NobtRepositoryCommandInvokerFactory(config).create();

        http = Service.ignite();

        new NobtRestApi(
                http,
                nobtRepositoryCommandInvoker,
                new BodyParser(objectMapper, validator),
                objectMapper,
                new NobtFactory()
        ).run(config.port());
    }

    @AfterClass
    public static void cleanupEnvironment() throws IOException {
        http.stop();
        migrationService.clean();
    }

    protected RequestSpecification documentationSpec;
    private Client client;

    @Before
    public void setUp() throws Exception {
        this.documentationSpec = new RequestSpecBuilder()
                .addFilter(documentationConfiguration(restDocumentation))
                .build();

        client = new Client(config.port());
    }

    @Test
    public void shouldCreateNewNobt() throws Exception {

        given(this.documentationSpec)
                .port(config.port())
                .filter(
                        document("create-nobt",
                                preprocessRequest(modifyUris().scheme("http").host("localhost").port(DOCUMENTED_PORT)),
                                requestFields(
                                        fieldWithPath("nobtName").description("The name of the nobt."),
                                        fieldWithPath("currency").description("The currency of this nobt."),
                                        fieldWithPath("explicitParticipants").optional().description("An array of people that should always be listed as participants, no matter if they ever participate as debtee / debtor or not.")
                                )
                        )
                )
                .body("{\n" +
                        "  \"nobtName\": \"Grillfeier\",\n" +
                        "  \"currency\": \"EUR\",\n" +
                        "  \"explicitParticipants\": [\n" +
                        "    \"Thomas\",\n" +
                        "    \"Martin\",\n" +
                        "    \"Lukas\"\n" +
                        "  ]\n" +
                        "}")
                .contentType("application/json")

                .when()

                .post("/nobts")

                .then()

                .statusCode(201)
                .header("Location", not(isEmptyOrNullString()));
    }

    @Test
    public void shouldAddNewExpense() throws Exception {

        final String nobtId = client.createGrillfeierNobt();

        given(this.documentationSpec)
                .port(config.port())
                .filter(
                        document("create-expense",
                                preprocessRequest(modifyUris().scheme("http").host("localhost").port(DOCUMENTED_PORT)),
                                requestFields(
                                        fieldWithPath("name").description("Human readable description of the expense."),
                                        fieldWithPath("debtee").description("The name of the person who made the expense."),
                                        fieldWithPath("currencyInformation").optional().type(JsonFieldType.OBJECT).description("An object defining the conversion information to the currency of the associated nobt. If this object is present, the amounts of this expense are to be interpreted in the given currency. If this object is not present, it is assumed that the currency of this expense is equal to the currency of the nobt, hence the conversion rate is 1."),
                                        fieldWithPath("currencyInformation.foreignCurrency").type(JsonFieldType.STRING).description("The ISO-4217 code of the currency."),
                                        fieldWithPath("currencyInformation.rate").type(JsonFieldType.NUMBER).description("The conversion rate for converting the amounts of this expense into the currency of the associated nobt."),
                                        fieldWithPath("splitStrategy").description("A simple text field for storing an identifier that indicates which `strategy` the user picked to split the expense."),
                                        fieldWithPath("date").description("The date on which the expense was spent. Takes any ISO6801-compliant string."),
                                        fieldWithPath("shares").description("All shares that form this expense. Note that, as in the example above, there is an extra share that mentions Thomas as the debtor, despite he is also the debtee of the expense. The above setup equals splitting the bill in three parts.")
                                )
                        )
                )
                .body("{\n" +
                        "  \"name\": \"Fleisch\",\n" +
                        "  \"debtee\": \"Thomas\",\n" +
                        "  \"splitStrategy\": \"EVENLY\",\n" +
                        "  \"currencyInformation\": {\n" +
                        "    \"foreignCurrency\": \"USD\",\n" +
                        "    \"rate\": 1.15\n" +
                        "  },\n" +
                        "  \"date\": \"2016-10-05\",\n" +
                        "  \"shares\": [\n" +
                        "    {\n" +
                        "      \"debtor\": \"David\",\n" +
                        "      \"amount\": 4\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"debtor\": \"Thomas\",\n" +
                        "      \"amount\": 4\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"debtor\": \"Matthias\",\n" +
                        "      \"amount\": 4\n" +
                        "    }\n" +
                        "  ]\n" +
                        "}")
                .contentType("application/json")

                .when()

                .post("/nobts/{nobtId}/expenses", nobtId)

                .then()

                .statusCode(201);
    }

    @Test
    public void shouldAddNewPayment() throws Exception {

        final String nobtId = client.createGrillfeierNobt();

        given(this.documentationSpec)
                .port(config.port())
                .filter(
                        document("add-payment",
                                preprocessRequest(modifyUris().scheme("http").host("localhost").port(DOCUMENTED_PORT)),
                                requestFields(
                                        fieldWithPath("sender").description("The sender of the payment."),
                                        fieldWithPath("recipient").description("The recipient of the payment."),
                                        fieldWithPath("amount").description("The total of this payment."),
                                        fieldWithPath("date").description("The date which should be stored for the payment. Takes any ISO6801-compliant string."),
                                        fieldWithPath("description").optional().description("A human readable description of the payment."),
                                        fieldWithPath("currencyInformation").optional().type(JsonFieldType.OBJECT).description("An object defining the conversion information to the currency of the associated nobt. If this object is present, the amounts of this expense are to be interpreted in the given currency. If this object is not present, it is assumed that the currency of this expense is equal to the currency of the nobt, hence the conversion rate is 1."),
                                        fieldWithPath("currencyInformation.foreignCurrency").type(JsonFieldType.STRING).description("The ISO-4217 code of the currency."),
                                        fieldWithPath("currencyInformation.rate").type(JsonFieldType.NUMBER).description("The conversion rate for converting the amounts of this expense into the currency of the associated nobt.")
                                )
                        )
                )
                .body("{\n" +
                        "  \"sender\": \"David\",\n" +
                        "  \"recipient\": \"Thomas\",\n" +
                        "  \"amount\": 4,\n" +
                        "  \"description\": \"Settle debts\",\n" +
                        "  \"date\": \"2017-10-10\",\n" +
                        "  \"currencyInformation\": {\n" +
                        "    \"foreignCurrency\": \"EUR\",\n" +
                        "    \"rate\": 1.0\n" +
                        "  }\n" +
                        "}")
                .contentType("application/json")

                .when()

                .post("/nobts/{nobtId}/payments", nobtId)

                .then()

                .statusCode(201);
    }

    @Test
    public void shouldGetCompleteNobt() throws Exception {

        final String nobtId = client.createGrillfeierNobt();
        client.addFleischExpense(nobtId);
        client.addPayment(nobtId, "David", "Thomas", 4);

        given(this.documentationSpec)
                .port(config.port())
                .filter(
                        document("get-nobt",
                                preprocessRequest(modifyUris().scheme("http").host("localhost").port(DOCUMENTED_PORT)),
                                responseFields(
                                        fieldWithPath("id").type(JsonFieldType.STRING).description("The id of the nobt. Can be used to construct URIs to the various endpoints of the API."),
                                        fieldWithPath("name").type(JsonFieldType.STRING).description("The name of the nobt."),
                                        fieldWithPath("currency").type(JsonFieldType.STRING).description("The currency of this nobt."),
                                        fieldWithPath("createdOn").type(JsonFieldType.STRING).description("An ISO6801-compliant timestamp when the nobt was created."),

                                        fieldWithPath("expenses").type(JsonFieldType.ARRAY).description("All expenses associated with this nobt."),
                                        fieldWithPath("expenses[].id").type(JsonFieldType.NUMBER).description("The id of the expense."),
                                        fieldWithPath("expenses[].createdOn").type(JsonFieldType.STRING).description("An ISO6801-compliant timestamp when the expense was created."),
                                        fieldWithPath("expenses[].date").type(JsonFieldType.STRING).description("The given date of the expense."),
                                        fieldWithPath("expenses[].name").type(JsonFieldType.STRING).description("The given name of the expense."),
                                        fieldWithPath("expenses[].debtee").type(JsonFieldType.STRING).description("The debtee of the expense."),
                                        fieldWithPath("expenses[].splitStrategy").type(JsonFieldType.STRING).description("The split strategy that was chosen for this expense."),
                                        fieldWithPath("expenses[].shares").type(JsonFieldType.ARRAY).description("The array of shares this expense consists of."),
                                        fieldWithPath("expenses[].shares[].debtor").type(JsonFieldType.STRING).description("The debtor of this share."),
                                        fieldWithPath("expenses[].shares[].amount").type(JsonFieldType.NUMBER).description("The amount of this share."),

                                        fieldWithPath("payments").type(JsonFieldType.ARRAY).description("All payments associated with this nobt."),
                                        fieldWithPath("payments[].id").type(JsonFieldType.NUMBER).description("The id of the payment."),
                                        fieldWithPath("payments[].createdOn").type(JsonFieldType.STRING).description("An ISO6801-compliant timestamp when the payment was created."),
                                        fieldWithPath("payments[].date").type(JsonFieldType.STRING).description("The given date of the payment."),
                                        fieldWithPath("payments[].sender").type(JsonFieldType.STRING).description("The sender of the payment."),
                                        fieldWithPath("payments[].recipient").type(JsonFieldType.STRING).description("The recipient of the payment."),
                                        fieldWithPath("payments[].amount").type(JsonFieldType.NUMBER).description("The total of this payment."),
                                        fieldWithPath("payments[].description").optional().type(JsonFieldType.STRING).description("An optional description of the payment."),

                                        fieldWithPath("participatingPersons").type(JsonFieldType.ARRAY).description("An array of persons participating in this nobt. Contains the explicit participants passed to the API on creation of the nobt and all persons that take part in this nobt either as debtee or as debtor. Each name is only contained once."),

                                        fieldWithPath("debts").type(JsonFieldType.ARRAY).description("Contains an array describing the current debts between the persons in the nobt."),
                                        fieldWithPath("debts[].debtor").type(JsonFieldType.STRING).description("The person who has to pay / give money in this transaction."),
                                        fieldWithPath("debts[].amount").type(JsonFieldType.NUMBER).description("The amount the debtor has to pay."),
                                        fieldWithPath("debts[].debtee").type(JsonFieldType.STRING).description("The person who receives the money.")
                                )
                        )
                )

                .when()

                .get("/nobts/{nobtId}", nobtId)

                .then()

                .statusCode(200);
    }

    @Test
    public void shouldDeleteExpense() throws Exception {

        final String nobtId = client.createGrillfeierNobt();
        client.addFleischExpense(nobtId);

        final Long idOfFirstExpense = client.getNobt(nobtId).jsonPath().getLong("expenses[0].id");


        given(this.documentationSpec)
                .port(config.port())
                .filter(
                        document("delete-expense",
                                preprocessRequest(modifyUris().scheme("http").host("localhost").port(DOCUMENTED_PORT))
                        )
                )

                .when()

                .delete("/nobts/{nobtId}/expenses/{expenseId}", nobtId, idOfFirstExpense)

                .then()

                .statusCode(204);



        client.getNobt(nobtId)
                .then()
                .body("expenses", response -> hasSize(0));
    }

    @Test
    public void deletingExpenseThatDoesNotExistRespondsWith204() throws Exception {

        final String nobtId = client.createGrillfeierNobt();
        client.addFleischExpense(nobtId);

        given(this.documentationSpec)
                .port(config.port())

                .when()

                .delete("/nobts/{nobtId}/expenses/{expenseId}", nobtId, 104)

                .then()

                .statusCode(204);
    }

    @Test
    public void shouldRejectExpenseWithDuplicateDebtor() throws Exception {

        final String nobtId = client.createGrillfeierNobt();

        given(this.documentationSpec)
                .port(config.port())
                .filter(
                        document("duplicate-debtor",
                                preprocessRequest(modifyUris().scheme("http").host("localhost").port(DOCUMENTED_PORT)),
                                responseFields(
                                        fieldWithPath("title").description("A short description of the error."),
                                        fieldWithPath("status").description("The status code for this error."),
                                        fieldWithPath("detail").description("A more detailed message concerning this error."),
                                        fieldWithPath("errors[].property").description("A simple path expression for the property that caused the validation error."),
                                        fieldWithPath("errors[].reason").description("The reason why the supplied value is invalid.")
                                )
                        )
                )
                .body("{\n" +
                        "  \"name\": \"Fleisch\",\n" +
                        "  \"debtee\": \"Thomas\",\n" +
                        "  \"splitStrategy\": \"EVENLY\",\n" +
                        "  \"date\": \"2016-10-05\",\n" +
                        "  \"shares\": [\n" +
                        "    {\n" +
                        "      \"debtor\": \"Thomas\",\n" +
                        "      \"amount\": 2\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"debtor\": \"Thomas\",\n" +
                        "      \"amount\": 2\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"debtor\": \"Matthias\",\n" +
                        "      \"amount\": 4\n" +
                        "    }\n" +
                        "  ]\n" +
                        "}")
                .contentType("application/json")

                .when()

                .post("/nobts/{nobtId}/expenses", nobtId)

                .then()

                .statusCode(400);
    }

    @Test
    public void shouldRejectExpenseWithInvalidConversionInformation() throws Exception {

        final String nobtId = client.createGrillfeierNobt();

        given(this.documentationSpec)
                .port(config.port())
                .body("{\n" +
                        "  \"name\": \"Fleisch\",\n" +
                        "  \"debtee\": \"Thomas\",\n" +
                        "  \"splitStrategy\": \"EVENLY\",\n" +
                        "  \"conversionInformation\": {\n" +
                        "    \"foreignCurrency\": \"EUR\",\n" +
                        "    \"rate\": 1.5\n" +
                        "  },\n" +
                        "  \"date\": \"2016-10-05\",\n" +
                        "  \"shares\": [\n" +
                        "    {\n" +
                        "      \"debtor\": \"Thomas\",\n" +
                        "      \"amount\": 2\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"debtor\": \"Thomas\",\n" +
                        "      \"amount\": 2\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"debtor\": \"Matthias\",\n" +
                        "      \"amount\": 4\n" +
                        "    }\n" +
                        "  ]\n" +
                        "}")
                .contentType("application/json")

                .when()

                .post("/nobts/{nobtId}/expenses", nobtId)

                .then()

                .statusCode(400);
    }
}
