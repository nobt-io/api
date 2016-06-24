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

    public static Matcher<Nobt> hasName(String name) {
        return new FeatureMatcher<Nobt, String>(is(name), "nobt with name", "name") {
            @Override
            protected String featureValueOf(Nobt actual) {
                return actual.getName();
            }
        };
    }

    public static Matcher<Nobt> hasExplicitParticipantWithName(String participant) {
        return new FeatureMatcher<Nobt, Set<Person>>(hasItem(Person.forName(participant)), "participant with name", "participants") {
            @Override
            protected Set<Person> featureValueOf(Nobt actual) {
                return actual.getParticipatingPersons();
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
