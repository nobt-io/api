package io.nobt.rest.payloads;

import java.util.Optional;

import javax.validation.ConstraintViolation;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Thomas Eizinger, Senacor Technologies AG.
 */
public class SimpleViolation {

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
