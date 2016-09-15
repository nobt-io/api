package io.nobt.application.env;

import io.nobt.persistence.DatabaseConfig;

import java.net.URI;

public class CFConnectionStringAdapter implements DatabaseConfig {

    private static final String JDBC_SCHEME = "jdbc:";

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

        URI uri1 = URI.create(normalizedURI);

        final String[] userInfo = uri1.getUserInfo().split(":");

        final String urlTemplate = "jdbc:postgresql://%s:%s%s";

        final String jdbcConnectionURL = String.format(urlTemplate, uri1.getHost(), uri1.getPort(), uri1.getPath());

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
