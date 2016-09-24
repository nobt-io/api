package io.nobt.application.env;

import io.nobt.persistence.DatabaseConfig;

import java.net.URI;

/**
 * Adapter class that is needed because cloudfoundry does not give us a proper connection-string.
 */
public class CFConnectionStringAdapter implements DatabaseConfig {

    private static final String JDBC_SCHEME = "jdbc:";
    private static final String JDBC_URL_TEMPLATE = "jdbc:postgresql://%s:%s%s";

    private final String username;
    private final String password;
    private final String url;

    private CFConnectionStringAdapter(String username, String password, String url) {
        this.username = username;
        this.password = password;
        this.url = url;
    }

    public static DatabaseConfig parse(final String uriString) {

        final String normalizedURI = hasJdbcScheme(uriString) ? normalize(uriString) : uriString;
        final URI uri = URI.create(normalizedURI);

        final String jdbcConnectionURL = String.format(JDBC_URL_TEMPLATE, uri.getHost(), uri.getPort(), uri.getPath());
        final String[] userInfo = uri.getUserInfo().split(":");

        return new CFConnectionStringAdapter(userInfo[0], userInfo[1], jdbcConnectionURL);
    }

    private static boolean hasJdbcScheme(final String uri) {
        return uri.startsWith(JDBC_SCHEME);
    }

    private static String normalize(String uri) {
        return uri.replace(JDBC_SCHEME, "");
    }

    @Override
    public String url() {
        return url;
    }

    @Override
    public String username() {
        return username;
    }

    @Override
    public String password() {
        return password;
    }
}
