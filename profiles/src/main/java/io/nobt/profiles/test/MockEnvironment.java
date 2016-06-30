package io.nobt.profiles.test;

import io.nobt.profiles.Environment;

import java.util.HashMap;
import java.util.Map;

public class MockEnvironment implements Environment {

    private static final Map<String, String> environment = new HashMap<>();

    public static void setVariable(String key, String value) {
        environment.put(key, value);
    }

    public static void removeVariable(String key) {
        environment.remove(key);
    }

    @Override
    public String getVariable(String key) {
        return environment.get(key);
    }
}
