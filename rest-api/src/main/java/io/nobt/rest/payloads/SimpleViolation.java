package io.nobt.rest.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SimpleViolation {

    @JsonProperty("property")
    private final String property;

    @JsonProperty("value")
    private final String value;

    @JsonProperty("message")
    private final String message;

    public SimpleViolation(String property, String value, String message) {
        this.property = property;
        this.value = value;
        this.message = message;
    }
}
