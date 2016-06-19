package io.nobt.rest.handler;

import spark.Request;
import spark.Response;
import spark.Route;

import java.util.UUID;

public class GetPersonsHandler implements Route {

    @Override
    public Object handle(Request request, Response response) throws Exception {

        final UUID nobtId = UUID.fromString(request.params(":nobtId"));

        response.status(303);
        response.header("Location", request.url() + "/" + nobtId);

        return null;
    }
}
