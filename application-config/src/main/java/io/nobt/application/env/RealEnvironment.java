package io.nobt.application.env;

public class RealEnvironment implements Environment {
    @Override
    public String getValue(String key) {
        return System.getenv(key);
    }
}
