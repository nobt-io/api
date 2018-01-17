package io.nobt.rest.links;

import spark.Request;

public class BasePath {

    private final String protocol;
    private final String host;

    public BasePath(String protocol, String host) {
        this.protocol = protocol;
        this.host = host;
    }

    public static BasePath parse(Request request, String schemeOverrideHeader) {

        final String protocol = determineProtocol(request, schemeOverrideHeader);
        final String host = request.host();

        return new BasePath(protocol, host);

    }

    private static String determineProtocol(Request request, String schemeOverrideHeader) {
        final String forwardedProtocol = request.headers(schemeOverrideHeader);

        if (forwardedProtocol != null && !forwardedProtocol.isEmpty()) {
            return forwardedProtocol;
        }

        return request.scheme();
    }

    public String asString() {
        return String.format("%s://%s", protocol, host);
    }

    @Override
    public String toString() {
        return String.format("BasePath{%s}", asString());
    }
}
