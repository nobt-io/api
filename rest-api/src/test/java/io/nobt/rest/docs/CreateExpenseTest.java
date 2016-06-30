package io.nobt.rest.docs;

import io.nobt.core.domain.Nobt;
import io.nobt.core.domain.Person;
import io.nobt.util.Sets;
import org.junit.Test;

import java.util.Set;

import static com.jayway.restassured.RestAssured.given;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;
import static org.springframework.restdocs.restassured.operation.preprocess.RestAssuredPreprocessors.modifyUris;

public class CreateExpenseTest extends AbstractApiTest {

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
            .body("{ \"name\":\"Fleisch\",  \"amount\": 12.99, \"debtee\": \"Thomas\", \"debtors\": [\"Thomas\", \"Martin\", \"Lukas\"] }")
            .contentType("application/json")
        .when()
            .post("/nobts/" + nobt.getId() + "/expenses")
        .then()
            .statusCode(201);
    }
}
