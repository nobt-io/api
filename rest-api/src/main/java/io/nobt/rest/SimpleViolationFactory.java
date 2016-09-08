package io.nobt.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.nobt.rest.payloads.SimpleViolation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
import java.util.Optional;

public class SimpleViolationFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleViolationFactory.class);

    private final ObjectMapper objectMapper;

    public SimpleViolationFactory(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public SimpleViolation create(ConstraintViolation<?> violation) {

        String property = violation.getPropertyPath().toString();
        String value = Optional.ofNullable(violation.getInvalidValue()).map( this::toJson ).orElse(null);
        String message = violation.getMessage();

        return new SimpleViolation(property, value, message);
    }

    private String toJson(Object o) {
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(o);
        } catch (JsonProcessingException e) {
            LOGGER.error("Failed to serialize invalid value as JSON", e);
            return null;
        }
    }
}
