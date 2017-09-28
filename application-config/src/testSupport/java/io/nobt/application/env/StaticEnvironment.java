package io.nobt.application.env;

import java.util.Map;

public final class StaticEnvironment implements Environment {

    private final Map<String, String> values;

    public StaticEnvironment(Map<String, String> values) {
        this.values = values;
    }

    @Override
    public String getValue(String key) {
        return values.get(key);
    }
}
