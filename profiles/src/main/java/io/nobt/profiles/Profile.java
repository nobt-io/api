package io.nobt.profiles;

import io.nobt.profiles.spi.ActiveProfileEvaluatorLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;

public enum Profile {

    STANDALONE,
    LOCAL,
    CLOUD;

    private static final Logger LOGGER = LogManager.getLogger(Profile.class);

    public static final Profile DEFAULT = STANDALONE;

    public static Profile getCurrentProfile() {
        return ActiveProfileEvaluatorLoader.load().determineActiveProfile().orElseGet(() -> {
            LOGGER.info("Active profile could not be evaluated. Defaulting to {}", DEFAULT);
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
