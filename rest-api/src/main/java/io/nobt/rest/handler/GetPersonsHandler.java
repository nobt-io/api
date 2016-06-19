package io.nobt.rest.handler;

import spark.Request;
import spark.Response;
import spark.Route;

public class GetPersonsHandler implements Route {

    @Override
    public Object handle(Request request, Response response) throws Exception {

        response.status(303);
        response.header("Location", request.url().replace("/persons", ""));

        return "";
    }
}
