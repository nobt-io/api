package io.nobt.profiles;

import java.util.Optional;

public interface ActiveProfileEvaluator {

    Optional<Profile> determineActiveProfile();

}
