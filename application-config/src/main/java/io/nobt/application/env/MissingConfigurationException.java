package io.nobt.application.env;

public class MissingConfigurationException extends RuntimeException {

    private final Config.Keys configurationKey;

    public MissingConfigurationException(Config.Keys key) {
        this.configurationKey = key;
    }

    @Override
    public String getMessage() {
        return String.format("Missing configuration parameter for key '%s'", configurationKey);
    }
}
