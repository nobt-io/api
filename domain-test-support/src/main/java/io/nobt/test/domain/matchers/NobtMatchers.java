package io.nobt.test.domain.matchers;

import io.nobt.core.domain.Expense;
import io.nobt.core.domain.Nobt;
import io.nobt.core.domain.Person;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;

import java.util.Set;

public final class NobtMatchers {

    public static Matcher<Nobt> hasName(final Matcher<String> subMatcher) {
        return new FeatureMatcher<Nobt, String>(subMatcher, "name", "name") {
            @Override
            protected String featureValueOf(Nobt actual) {
                return actual.getName();
            }
        };
    }

    public static Matcher<Nobt> hasParticipants(Matcher<? super Set<Person>> subMatcher) {
        return new FeatureMatcher<Nobt, Set<Person>>(subMatcher, "participants", "participants") {
            @Override
            protected Set<Person> featureValueOf(Nobt actual) {
                return actual.getParticipatingPersons();
            }
        };
    }

    public static Matcher<Nobt> hasExpenses(final Matcher<? super Iterable<Expense>> subMatcher) {
        return new FeatureMatcher<Nobt, Set<Expense>>(subMatcher, "expenses", "expenses") {
            @Override
            protected Set<Expense> featureValueOf(Nobt actual) {
                return actual.getExpenses();
            }
        };
    }
}
