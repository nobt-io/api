package io.nobt.rest;

import spark.Filter;
import spark.Request;
import spark.Response;

import java.util.Arrays;
import java.util.List;

public class CORSHandler implements Filter {

    private final List<String> originWhitelist = Arrays.asList(
            "http://localhost:3000",
            "http://nobt.io",
            "https://nobt.io"
    );

    @Override
    public void handle(Request request, Response response) throws Exception {

        final String origin = request.headers("Origin");

        if (originWhitelist.contains(origin)) {
            response.header("Access-Control-Allow-Origin", origin);
            response.header("Access-Control-Request-Method", "*");
        }
    }
}
