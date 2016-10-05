package io.nobt.test.domain.matchers;

import io.nobt.core.domain.Expense;
import io.nobt.core.domain.Person;
import io.nobt.core.domain.Share;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;

import java.time.LocalDate;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public final class ExpenseMatchers {

    public static Matcher<Expense> hasDebtee(final Matcher<Person> subMatcher) {
        return new FeatureMatcher<Expense, Person>(subMatcher, "debtee", "debtee") {
            @Override
            protected Person featureValueOf(Expense actual) {
                return actual.getDebtee();
            }
        };
    }

    public static Matcher<Expense> onDate(final Matcher<LocalDate> dateMatcher) {
        return new FeatureMatcher<Expense, LocalDate>(dateMatcher, "date", "date") {
            @Override
            protected LocalDate featureValueOf(Expense actual) {
                return actual.getDate();
            }
        };
    }

    public static Matcher<Expense> hasShares(final Matcher<? super Set<Share>> sharesMatcher) {
        return new FeatureMatcher<Expense, Set<Share>>(sharesMatcher, "shares", "shares") {
            @Override
            protected Set<Share> featureValueOf(Expense actual) {
                return actual.getShares();
            }
        };
    }

    public static Matcher<Expense> hasDebtors(final Matcher<? super Set<Person>> iterableMatcher) {
        return new FeatureMatcher<Expense, Set<Person>>(iterableMatcher, "debtors", "debtors") {
            @Override
            protected Set<Person> featureValueOf(Expense actual) {
                return actual.getShares().stream().map(Share::getDebtor).collect(toSet());
            }
        };
    }
}
