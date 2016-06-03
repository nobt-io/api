package io.nobt.matchers;

import io.nobt.core.domain.Expense;
import io.nobt.core.domain.Nobt;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;

import java.util.Set;

import static org.hamcrest.Matchers.*;

public final class NobtMatchers {

    public static Matcher<Nobt> hasName(String name) {
        return new FeatureMatcher<Nobt, String>(is(name), "nobt with name", "name") {
            @Override
            protected String featureValueOf(Nobt actual) {
                return actual.getName();
            }
        };
    }

    public static Matcher<Nobt> hasExpense(Matcher<Expense> expenseMatcher) {
        return new FeatureMatcher<Nobt, Set<Expense>>(hasItem(expenseMatcher), "nobt with expense", "expenses") {
            @Override
            protected Set<Expense> featureValueOf(Nobt actual) {
                return actual.getExpenses();
            }
        };
    }

    public static Matcher<Nobt> hasExpense(Expense expense) {
        return new FeatureMatcher<Nobt, Set<Expense>>(hasItem(expense), "nobt with expense", "expenses") {
            @Override
            protected Set<Expense> featureValueOf(Nobt actual) {
                return actual.getExpenses();
            }
        };
    }
}
