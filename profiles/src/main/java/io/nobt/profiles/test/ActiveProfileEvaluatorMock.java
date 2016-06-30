package io.nobt.profiles.test;

import io.nobt.profiles.ActiveProfileEvaluator;
import io.nobt.profiles.Profile;

import java.util.Optional;

public class ActiveProfileEvaluatorMock implements ActiveProfileEvaluator {

    private static Profile profile;

    public static void setActiveProfile(Profile profile) {
        ActiveProfileEvaluatorMock.profile = profile;
    }

    public static void clearActiveProfile() {
        ActiveProfileEvaluatorMock.profile = null;
    }

    @Override
    public Optional<Profile> determineActiveProfile() {
        return Optional.ofNullable(profile);
    }
}
