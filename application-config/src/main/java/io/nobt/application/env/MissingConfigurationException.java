package io.nobt.application.env;

import java.util.function.Supplier;

public class MissingConfigurationException extends RuntimeException {

    private final Config.Keys configurationKey;

    public MissingConfigurationException(Config.Keys key) {
        this.configurationKey = key;
    }

    @Override
    public String getMessage() {
        return String.format("Missing configuration parameter for key '%s'", configurationKey);
    }

    public static Supplier<RuntimeException> missingConfigurationException(Config.Keys key) {
        return () -> new MissingConfigurationException(key);
    }
}
