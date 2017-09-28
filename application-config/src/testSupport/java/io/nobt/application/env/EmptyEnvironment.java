package io.nobt.application.env;

public final class EmptyEnvironment implements Environment {
    @Override
    public String getValue(String key) {
        return null;
    }
}
