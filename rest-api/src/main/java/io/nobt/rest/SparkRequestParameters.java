package io.nobt.rest;

import spark.Request;

public class SparkRequestParameters implements RequestParameters {

    private final Request currentRequest;
    private String schemeOverrideHeader;

    public SparkRequestParameters(Request currentRequest, String schemeOverrideHeader) {
        this.currentRequest = currentRequest;
        this.schemeOverrideHeader = schemeOverrideHeader;
    }

    @Override
    public String getScheme() {

        final String forwardedProtocol = currentRequest.headers(schemeOverrideHeader);

        if (forwardedProtocol != null && !forwardedProtocol.isEmpty()) {
            return forwardedProtocol;
        }

        return currentRequest.scheme();
    }

    @Override
    public String getHost() {
        return currentRequest.host();
    }
}
