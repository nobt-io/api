package io.nobt.matchers;

import io.nobt.core.domain.Person;
import io.nobt.core.domain.Share;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;

public final class ShareMatchers {

    public static Matcher<Share> hasDebtor(Matcher<Person> debtorMatcher) {
        return new FeatureMatcher<Share, Person>(debtorMatcher, "debtor", "debtor") {
            @Override
            protected Person featureValueOf(Share actual) {
                return actual.getDebtor();
            }
        };
    }
}
