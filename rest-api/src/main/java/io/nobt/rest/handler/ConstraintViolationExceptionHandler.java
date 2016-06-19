package io.nobt.rest.handler;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import spark.ExceptionHandler;
import spark.Request;
import spark.Response;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ConstraintViolationExceptionHandler implements ExceptionHandler {

    private final ObjectMapper objectMapper;

    public ConstraintViolationExceptionHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void handle(Exception e, Request request, Response response) {
        final Set<ConstraintViolation<?>> violations = ((ConstraintViolationException) e).getConstraintViolations();

        final List<SimpleViolation> simpleViolations = violations.stream().map(cv -> new SimpleViolation(cv.getPropertyPath(), cv.getInvalidValue().toString(), cv.getMessage())).collect(Collectors.toList());

        response.status(400);
        response.header("Content-Type", "application/json");

        try {
            response.body(objectMapper.writeValueAsString(simpleViolations));
        } catch (JsonProcessingException e1) {
            throw new IllegalStateException(e1);
        }
    }

    public static class SimpleViolation {

        @JsonProperty("property")
        private final String property;

        @JsonProperty("value")
        private final String value;

        @JsonProperty("message")
        private final String message;

        public SimpleViolation(Path propertyPath, String value, String message) {
            this.property = propertyPath.toString();
            this.value = value;
            this.message = message;
        }
    }
}
