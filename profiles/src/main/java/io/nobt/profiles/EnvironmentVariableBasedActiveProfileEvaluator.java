package io.nobt.profiles;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.Optional;

public class EnvironmentVariableBasedActiveProfileEvaluator implements ActiveProfileEvaluator {

    private static final Logger LOGGER = LogManager.getLogger(EnvironmentVariableBasedActiveProfileEvaluator.class);

    public static String ENVIRONMENT_VARIABLE = "profile";

    @Override
    public Optional<Profile> determineActiveProfile() {

        final String value = System.getenv(ENVIRONMENT_VARIABLE);

        LOGGER.info("Matching available profile names ({}) against value of profile environment variable '{}': {}", Arrays.toString(Profile.values()), ENVIRONMENT_VARIABLE, value);

        return Arrays.stream(Profile.values()).filter(p -> p.name().equalsIgnoreCase(value)).findFirst();
    }
}
