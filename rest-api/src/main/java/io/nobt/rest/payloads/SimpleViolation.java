package io.nobt.rest.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SimpleViolation {

    @JsonProperty("property")
    private final String property;

    @JsonProperty("message")
    private final String message;

    public SimpleViolation(String property, String message) {
        this.property = property;
        this.message = message;
    }
}
