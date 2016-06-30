package io.nobt.rest.docs;

import org.junit.Test;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;
import static org.springframework.restdocs.restassured.operation.preprocess.RestAssuredPreprocessors.modifyUris;

public class CreateNobtTest extends AbstractApiTest {

    @Test
    public void shouldCreateNewNobt() throws Exception {

        given(this.documentationSpec).
                filter(document("create-nobt", preprocessRequest(modifyUris().scheme("http").host("localhost").port(8080)))).
                body("{ \"nobtName\":\"Grillfeier\", \"explicitParticipants\": [\"Thomas\", \"Martin\", \"Lukas\"] }").
        when().
                post("/nobts").
        then().
                header("Location", not(isEmptyOrNullString()));
    }
}
