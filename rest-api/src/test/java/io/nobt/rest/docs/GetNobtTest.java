package io.nobt.rest.docs;

import io.nobt.core.domain.Nobt;
import io.nobt.core.domain.Person;
import io.nobt.util.Sets;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Set;

import static com.jayway.restassured.RestAssured.given;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;
import static org.springframework.restdocs.restassured.operation.preprocess.RestAssuredPreprocessors.modifyUris;

public class GetNobtTest extends AbstractApiTest {

    @Test
    public void shouldCreateeNewExpense() throws Exception {

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
            .get("/nobts/" + nobt.getId())
        .then()
            .statusCode(200);
    }
}