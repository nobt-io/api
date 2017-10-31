package io.nobt.rest;

import com.jayway.restassured.response.Response;

import static com.jayway.restassured.RestAssured.given;

public class Client {

    private final int port;

    public Client(int port) {
        this.port = port;
    }

    public String createGrillfeierNobt() {
        return given()
                .port(port)
                .body("{\n" +
                        "  \"nobtName\": \"Grillfeier\",\n" +
                        "  \"currency\": \"EUR\",\n" +
                        "  \"explicitParticipants\": [\n" +
                        "    \"Thomas\",\n" +
                        "    \"Martin\",\n" +
                        "    \"Lukas\"\n" +
                        "  ]\n" +
                        "}")
                .post("/nobts")
                .jsonPath()
                .getString("id");
    }

    public Response getNobt(String id) {
        return given()
                .port(port)
                .get("/nobts/{id}", id);
    }

    public void addFleischExpense(String nobtId) {
        given().port(port)
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
                .post("/nobts/{id}/expenses", nobtId);
    }

    public void addPayment(String nobtId, String from, String to, int amount) {
        given().port(port)
                .body("{\n" +
                        "  \"sender\": \"" + from + "\",\n" +
                        "  \"recipient\": \"" + to + "\",\n" +
                        "  \"amount\": " + amount + "\n" +
                        "}")
                .post("/nobts/{id}/expenses", nobtId);
    }
}
