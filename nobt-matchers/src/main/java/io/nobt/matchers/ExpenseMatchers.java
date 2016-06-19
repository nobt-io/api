package io.nobt.matchers;

import io.nobt.core.domain.Expense;
import io.nobt.core.domain.Person;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;

import java.util.Set;

import static java.util.stream.Collectors.*;
import static org.hamcrest.Matchers.*;

public final class ExpenseMatchers {

    public static Matcher<Expense> hasDebteeWithName(String debteeName) {
        return new FeatureMatcher<Expense, String>(is(debteeName), "debtee with name", "debtee-name") {
            @Override
            protected String featureValueOf(Expense actual) {
                return actual.getDebtee().getName();
            }
        };
    }

    public static Matcher<Expense> hasDebtorWithName(String debtorName) {
        return new FeatureMatcher<Expense, Set<String>>(hasItem(debtorName), "debtor with name", "debtors") {
            @Override
            protected Set<String> featureValueOf(Expense actual) {
                return actual.getDebtors().stream().map(Person::getName).collect(toSet());
            }
        };
    }

    public static Matcher<Expense> hasNumberOfDebtors(int numberOfDebtors) {
        return new FeatureMatcher<Expense, Set<String>>(hasSize(numberOfDebtors), "number of debtors", "debtors") {
            @Override
            protected Set<String> featureValueOf(Expense actual) {
                return actual.getDebtors().stream().map(Person::getName).collect(toSet());
            }
        };
    }
}
