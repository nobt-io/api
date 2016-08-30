package io.nobt.dbconfig.cloud;

import java.net.URI;

public class CFConnectionStringAdapter {

    private final String username;
    private final String password;
    private final String url;

    private CFConnectionStringAdapter(String username, String password, String url) {
        this.username = username;
        this.password = password;
        this.url = url;
    }

    public static CFConnectionStringAdapter parse(URI uri) {

        final String[] userInfo = uri.getUserInfo().split(":");

        final String urlTemplate = "jdbc:postgresql://%s:%s%s";

        final String url = String.format(urlTemplate, uri.getHost(), uri.getPort(), uri.getPath());

        return new CFConnectionStringAdapter(userInfo[0], userInfo[1], url);
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
