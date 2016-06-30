package io.nobt.rest;

import io.nobt.profiles.Environment;

public class SystemEnvironment implements Environment {
    @Override
    public String getVariable(String key) {
        return System.getenv(key);
    }
}
