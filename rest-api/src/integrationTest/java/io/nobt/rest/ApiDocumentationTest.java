package io.nobt.rest;

import com.jayway.restassured.builder.RequestSpecBuilder;
import com.jayway.restassured.specification.RequestSpecification;
import io.nobt.core.domain.Nobt;
import io.nobt.core.domain.NobtFactory;
import io.nobt.core.domain.NobtId;
import io.nobt.core.domain.Person;
import io.nobt.util.Sets;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.restdocs.JUnitRestDocumentation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.Set;

import static com.jayway.restassured.RestAssured.given;
import static io.nobt.test.domain.factories.ShareFactory.share;
import static io.nobt.test.domain.factories.StaticPersonFactory.*;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.restassured.operation.preprocess.RestAssuredPreprocessors.modifyUris;

public class ApiDocumentationTest extends ApiIntegrationTestBase {

    private static final int DOCUMENTED_PORT = 80;

    @Rule
    public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("build/generated-snippets");

    protected RequestSpecification documentationSpec;
    private NobtFactory nobtFactory;

    @Before
    public void setUp() throws Exception {
        this.documentationSpec = new RequestSpecBuilder()
                .addFilter(documentationConfiguration(restDocumentation))
                .build();

        nobtFactory = new NobtFactory();
    }

    @Test
    public void shouldCreateNewNobt() throws Exception {

        given(this.documentationSpec)
                .port(ACTUAL_PORT)
                .filter(
                        document("create-nobt",
                                preprocessRequest(modifyUris().scheme("http").host("localhost").port(DOCUMENTED_PORT)),
                                requestFields(
                                        fieldWithPath("nobtName").description("The name of the nobt."),
                                        fieldWithPath("explicitParticipants").optional().description("An array of people that should always be listed as participants, no matter if they ever participate as debtee / debtor or not.")
                                )
                        )
                )
                .body("{\n" +
                        "  \"nobtName\": \"Grillfeier\",\n" +
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

        final Set<Person> explicitParticipants = Sets.newHashSet(thomas, martin, lukas);

        final NobtId id = nobtRepository.save(nobtFactory.create("Grillfeier", explicitParticipants));

        given(this.documentationSpec)
                .port(ACTUAL_PORT)
                .filter(
                        document("create-expense",
                                preprocessRequest(modifyUris().scheme("http").host("localhost").port(DOCUMENTED_PORT)),
                                requestFields(
                                        fieldWithPath("name").description("Human readable description of the expense."),
                                        fieldWithPath("debtee").description("The name of the person who made the expense."),
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

                .post("/nobts/{nobtId}/expenses", id.toExternalIdentifier())

                .then()

                .statusCode(201);
    }

    @Test
    public void shouldGetCompleteNobt() throws Exception {

        final Set<Person> explicitParticipants = Sets.newHashSet(thomas, martin, lukas);

        final Nobt nobt = nobtFactory.create("Grillfeier", explicitParticipants);
        nobt.addExpense("Fleisch", "EVENLY", thomas, Sets.newHashSet(share(matthias, 3), share(lukas, 2), share(martin, 3), share(thomas, 3)), LocalDate.now());

        final NobtId id = nobtRepository.save(nobt);

        given(this.documentationSpec)
                .port(ACTUAL_PORT)
                .filter(
                        document("get-nobt",
                                preprocessRequest(modifyUris().scheme("http").host("localhost").port(DOCUMENTED_PORT)),
                                responseFields(
                                        fieldWithPath("id").description("The id of the nobt. Can be used to construct URIs to the various endpoints of the API."),
                                        fieldWithPath("name").description("The name of the nobt."),
                                        fieldWithPath("createdOn").description("An ISO6801-compliant timestamp when the nobt was created."),
                                        fieldWithPath("expenses").description("All expenses associated with this nobt."),
                                        fieldWithPath("participatingPersons").description("An array of persons participating in this nobt. Contains the explicit participants passed to the API on creation of the nobt and all persons that take part in this nobt either as debtee or as debtor. Each name is only contained once."),
                                        fieldWithPath("transactions").description("Contains an array of transactions that need to be made so that all debts are paid.")
                                )
                        )
                )

                .when()

                .get("/nobts/{nobtId}", id.toExternalIdentifier())

                .then()

                .statusCode(200);
    }

    @Test
    public void shouldRejectExpenseWithDuplicateDebtor() throws Exception {

        final Set<Person> explicitParticipants = Sets.newHashSet(thomas, martin, lukas);
        final NobtId id = nobtRepository.save(nobtFactory.create("Grillfeier", explicitParticipants));

        given(this.documentationSpec)
                .port(ACTUAL_PORT)
                .filter(
                        document("duplicate-debtor",
                                preprocessRequest(modifyUris().scheme("http").host("localhost").port(DOCUMENTED_PORT)),
                                responseFields(
                                        fieldWithPath("[].property").description("Describes the property in the request object which caused the validation to fail."),
                                        fieldWithPath("[].value").description("The value of the property as received by the server."),
                                        fieldWithPath("[].message").description("An error message that describes what went wrong.")
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

                .post("/nobts/{nobtId}/expenses", id.toExternalIdentifier())

                .then()

                .statusCode(400);
    }
}
