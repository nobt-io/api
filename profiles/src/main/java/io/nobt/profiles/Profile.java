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
    public static final String SYSTEM_PROPERTY = "profile";

    private final String systemPropertyValue;

    Profile(String systemPropertyValue) {
        this.systemPropertyValue = systemPropertyValue;
    }

    public static Profile getCurrentProfile() {

        final String systemPropertyValue = System.getProperty(SYSTEM_PROPERTY);

        return Arrays.stream(values()).filter( p -> p.systemPropertyValue.equals(systemPropertyValue)).findFirst().orElseGet( () -> {
            LOGGER.info("System property {} is not set. Defaulting to {}.", SYSTEM_PROPERTY, DEFAULT);
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
