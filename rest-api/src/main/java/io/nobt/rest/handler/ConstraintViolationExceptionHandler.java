package io.nobt.rest.handler;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import spark.ExceptionHandler;
import spark.Request;
import spark.Response;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;
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

        final List<SimpleViolation> simpleViolations = violations.stream().map(SimpleViolation::new).collect(Collectors.toList());

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

        public SimpleViolation(ConstraintViolation<?> violation) {
            this.property = violation.getPropertyPath().toString();
            this.value = Optional.ofNullable(violation.getInvalidValue()).map(Object::toString).orElse(null);
            this.message = violation.getMessage();
        }
    }
}
