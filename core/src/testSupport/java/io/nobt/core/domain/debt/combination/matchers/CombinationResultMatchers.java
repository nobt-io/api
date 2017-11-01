package io.nobt.core.domain.debt.combination.matchers;

import io.nobt.core.domain.debt.combination.CombinationResult;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;

public final class CombinationResultMatchers {

    public static Matcher<CombinationResult> hasChanges(Matcher<Boolean> subMatcher) {
        return new FeatureMatcher<CombinationResult, Boolean>(subMatcher, "hasChanges", "hasChanges") {
            @Override
            protected Boolean featureValueOf(CombinationResult actual) {
                return actual.hasChanges();
            }
        };
    }
}
