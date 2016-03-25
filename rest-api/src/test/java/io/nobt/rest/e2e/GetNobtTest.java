package io.nobt.rest.e2e;

import io.nobt.rest.NobtApplication;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import spark.Spark;

import static com.jayway.restassured.RestAssured.get;
import static com.jayway.restassured.RestAssured.given;
import static io.nobt.rest.JsonFileReader.readFromResource;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public class GetNobtTest {

	private String location;

	@Before
	public void setUp() throws Exception {
		NobtApplication.main(new String[]{});

		location = given().body(readFromResource("/create-nobt.json")).post("/nobts").header("Location");
		given().body(readFromResource("/create-expense.json")).post(location + "/expenses");
	}

	@After
	public void tearDown() throws Exception {
		Spark.stop();
	}

	@Test
	public void testShouldReturnNobt() throws Exception {
		get(location)
				.then()
				.statusCode(200)
				.body("", is(nullValue()));

		Assert.fail("Implement matching!");
	}
}
