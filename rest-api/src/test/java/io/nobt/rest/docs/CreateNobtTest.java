package io.nobt.rest.docs;

import org.junit.Test;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;
import static org.springframework.restdocs.restassured.operation.preprocess.RestAssuredPreprocessors.modifyUris;

public class CreateNobtTest extends AbstractApiTest {

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
}
