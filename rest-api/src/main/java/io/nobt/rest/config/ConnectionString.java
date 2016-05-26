package io.nobt.rest.config;

import java.net.URI;

public class ConnectionString {

    private final String username;
    private final String password;
    private final String url;

    private ConnectionString(String username, String password, String url) {
        this.username = username;
        this.password = password;
        this.url = url;
    }

    public static ConnectionString parse(URI uri) {

        final String[] userInfo = uri.getUserInfo().split(":");

        final String urlTemplate = "jdbc:postgresql://%s:%s%s";

        final String url = String.format(urlTemplate, uri.getHost(), uri.getPort(), uri.getPath());

        return new ConnectionString(userInfo[0], userInfo[1], url);
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getUrl() {
        return url;
    }
}
