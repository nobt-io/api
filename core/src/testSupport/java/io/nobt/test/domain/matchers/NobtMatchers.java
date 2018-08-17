package io.nobt.test.domain.matchers;

import io.nobt.core.domain.*;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;

import java.time.Instant;
import java.util.Set;

public final class NobtMatchers {

    public static Matcher<Nobt> hasId(final Matcher<NobtId> subMatcher) {
        return new FeatureMatcher<Nobt, NobtId>(subMatcher, "id", "id") {
            @Override
            protected NobtId featureValueOf(Nobt actual) {
                return actual.getId();
            }
        };
    }

    public static Matcher<Nobt> hasName(final Matcher<String> subMatcher) {
        return new FeatureMatcher<Nobt, String>(subMatcher, "name", "name") {
            @Override
            protected String featureValueOf(Nobt actual) {
                return actual.getName();
            }
        };
    }

    public static Matcher<Nobt> hasCurrency(final Matcher<CurrencyKey> subMatcher) {
        return new FeatureMatcher<Nobt, CurrencyKey>(subMatcher, "currency", "currency") {
            @Override
            protected CurrencyKey featureValueOf(Nobt actual) {
                return actual.getCurrencyKey();
            }
        };
    }

    public static Matcher<Nobt> hasCreationTime(final Matcher<Instant> subMatcher) {
        return new FeatureMatcher<Nobt, Instant>(subMatcher, "createdOn", "createdOn") {
            @Override
            protected Instant featureValueOf(Nobt actual) {
                return actual.getCreatedOn();
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

    public static Matcher<Nobt> hasDeletedExpenses(final Matcher<? super Iterable<DeletedExpense>> subMatcher) {
        return new FeatureMatcher<Nobt, Set<DeletedExpense>>(subMatcher, "deletedExpenses", "deletedExpenses") {
            @Override
            protected Set<DeletedExpense> featureValueOf(Nobt actual) {
                return actual.getDeletedExpenses();
            }
        };
    }

    public static Matcher<Nobt> hasPayments(final Matcher<? super Iterable<Payment>> subMatcher) {
        return new FeatureMatcher<Nobt, Set<Payment>>(subMatcher, "payments", "payments") {
            @Override
            protected Set<Payment> featureValueOf(Nobt actual) {
                return actual.getPayments();
            }
        };
    }
}
