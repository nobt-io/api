package io.nobt.rest.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SimpleViolation {

    @JsonProperty("property")
    private final String property;

    @JsonProperty("reason")
    private final String reason;

    public SimpleViolation(String property, String reason) {
        this.property = property;
        this.reason = reason;
    }
}
