package io.nobt.matchers;

import io.nobt.core.domain.Person;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;

public final class PersonMatchers {

    public static Matcher<Person> personWithName(Matcher<String> subMatcher) {
        return new FeatureMatcher<Person, String>(subMatcher, "name", "name") {
            @Override
            protected String featureValueOf(Person actual) {
                return actual.getName();
            }
        };
    }
}
