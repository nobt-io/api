package io.nobt.rest.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import spark.Request;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.io.IOException;
import java.util.Set;

public class BodyParser {

    private final ObjectMapper objectMapper;
    private final Validator validator;

    public BodyParser(ObjectMapper objectMapper, Validator validator) {
        this.objectMapper = objectMapper;
        this.validator = validator;
    }

    public <T> T parseBodyAs(Request request, Class<T> type) {

        String body = request.attribute("body");

        final T instance = readValue(type, body);

        final Set<ConstraintViolation<T>> violations = validator.validate(instance);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        return instance;
    }

    private <T> T readValue(Class<T> type, String body) {
        try {
            return objectMapper.readValue(body, type);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
