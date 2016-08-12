package io.nobt.matchers;

import io.nobt.core.domain.Expense;
import io.nobt.core.domain.Nobt;
import io.nobt.core.domain.Person;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;

import java.util.Set;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

public final class NobtMatchers {

    public static Matcher<Nobt> hasName(final Matcher<String> subMatcher) {
        return new FeatureMatcher<Nobt, String>(subMatcher, "nobt with name", "name") {
            @Override
            protected String featureValueOf(Nobt actual) {
                return actual.getName();
            }
        };
    }

    public static Matcher<Nobt> hasParticipants(Matcher<Set<Person>> subMatcher) {
        return new FeatureMatcher<Nobt, Set<Person>>(subMatcher, "participant with name", "participants") {
            @Override
            protected Set<Person> featureValueOf(Nobt actual) {
                return actual.getParticipatingPersons();
            }
        };
    }

    public static Matcher<Nobt> hasExpenses(final Matcher<Iterable<? super Expense>> subMatcher) {
        return new FeatureMatcher<Nobt, Set<Expense>>(subMatcher, "nobt with expense", "expenses") {
            @Override
            protected Set<Expense> featureValueOf(Nobt actual) {
                return actual.getExpenses();
            }
        };
    }
}
