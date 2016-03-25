package io.nobt.rest.e2e;

import io.nobt.rest.NobtApplication;
import org.junit.Before;
import org.junit.Test;

import static com.jayway.restassured.RestAssured.given;
import static io.nobt.rest.JsonFileReader.readFromResource;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class CreateNobtTest {

	@Before
	public void setUp() throws Exception {
		NobtApplication.main(new String[]{ });
	}

	@Test
	public void testCreateNobt() throws Exception {

		given()
				.body(readFromResource("/create-nobt.json"))
		.post("/nobts")
				.then()
					.statusCode(201)
					.header("Location", is(notNullValue()));
	}
}
