package io.nobt.profiles;

import java.util.Optional;

public interface ActiveProfileEvaluator extends Comparable<ActiveProfileEvaluator> {

    Optional<Profile> determineActiveProfile();

    int order();

    default int compareTo(ActiveProfileEvaluator o) {
        return Integer.compare(order(), o.order());
    }
}
