package io.nobt.rest.e2e;

import io.nobt.rest.NobtApplication;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import spark.Spark;

import static com.jayway.restassured.RestAssured.given;
import static io.nobt.rest.JsonFileReader.readFromResource;

public class CreateExpenseTest {

	private String location;

	@Before
	public void setUp() throws Exception {
		NobtApplication.main(new String[]{});

		location = given().body(readFromResource("/create-nobt.json")).post("/nobts").header("Location");
	}

	@After
	public void tearDown() throws Exception {
		Spark.stop();
	}

	@Test
	public void testShouldCreateExpense() throws Exception {
		given()
				.body(readFromResource("/create-expense.json"))
		.post(location + "/expenses")
				.then()
					.statusCode(201);
	}
}
