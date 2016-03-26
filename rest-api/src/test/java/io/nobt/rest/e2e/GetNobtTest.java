package io.nobt.rest.e2e;

import io.nobt.rest.NobtApplication;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import spark.Spark;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.config.JsonConfig.jsonConfig;
import static com.jayway.restassured.config.RestAssuredConfig.newConfig;
import static com.jayway.restassured.path.json.config.JsonPathConfig.NumberReturnType.DOUBLE;
import static io.nobt.rest.JsonFileReader.readFromResource;
import static org.hamcrest.Matchers.*;

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
		given()
				.config(newConfig().jsonConfig(jsonConfig().numberReturnType(DOUBLE)))
				.get(location)
					.then()
					.statusCode(200)
					.body("transactions", hasSize(2))
					.body("transactions[0].debtee", is("Thomas"))
					.body("transactions[0].amount", is(10.00d))
					.body("transactions[0].debtor", is("Matthias"))
					.body("transactions[1].debtee", is("Thomas"))
					.body("transactions[1].amount", is(10.00d))
					.body("transactions[1].debtor", is("Thomas B."))
					.body("id", is(notNullValue()))
					.body("expenses", is(notNullValue()));
	}
}
