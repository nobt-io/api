package io.nobt.profiles;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.function.Supplier;

public enum Profile {

    STANDALONE("standalone"),
    LOCAL("local"),
    CLOUD("cloud");

    private static final Logger LOGGER = LogManager.getLogger(Profile.class);

    public static final Profile DEFAULT = STANDALONE;
    public static final String ENV_VARIABLE = "profile";

    private final String envVariableValue;

    Profile(String envVariableValue) {
        this.envVariableValue = envVariableValue;
    }

    public static Profile getCurrentProfile() {

        final String actualEnvVariableValue = System.getenv(ENV_VARIABLE);

        return Arrays
                .stream(values())
                .filter(p -> p.envVariableValue.equalsIgnoreCase(actualEnvVariableValue))
                .findFirst()
                .orElseGet(() -> {
                    LOGGER.info("Environment variable '{}' is not set. Defaulting to profile {}.", ENV_VARIABLE, DEFAULT);
                    return DEFAULT;
                });
    }

    public <X extends T, T> T getProfileDependentValue(Supplier<X> standaloneValueProvider, Supplier<X> localValueProvider, Supplier<X> cloudValueProvider) {
        switch (this) {
            case STANDALONE:
                return standaloneValueProvider.get();

            case LOCAL:
                return localValueProvider.get();

            case CLOUD:
                return cloudValueProvider.get();

            default:
                throw new IllegalStateException("Unexpected profile instance.");
        }
    }
}
