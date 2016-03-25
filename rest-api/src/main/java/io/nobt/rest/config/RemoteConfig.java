package io.nobt.rest.config;

public class RemoteConfig extends Config {

    @Override
    public int getPort() {
        return Integer.parseInt(System.getenv("PORT"));
    }
}
