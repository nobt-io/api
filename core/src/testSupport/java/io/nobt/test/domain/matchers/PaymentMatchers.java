package io.nobt.test.domain.matchers;

import io.nobt.core.domain.Amount;
import io.nobt.core.domain.Payment;
import io.nobt.core.domain.Person;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;

public class PaymentMatchers {

    public static Matcher<Payment> hasId(final Matcher<Long> subMatcher) {
        return new FeatureMatcher<Payment, Long>(subMatcher, "id", "id") {
            @Override
            protected Long featureValueOf(Payment actual) {
                return actual.getId();
            }
        };
    }

    public static Matcher<Payment> hasSender(Matcher<Person> subMatcher) {
        return new FeatureMatcher<Payment, Person>(subMatcher, "sender", "sender") {
            @Override
            protected Person featureValueOf(Payment actual) {
                return actual.getSender();
            }
        };
    }

    public static Matcher<Payment> hasRecipient(Matcher<Person> subMatcher) {
        return new FeatureMatcher<Payment, Person>(subMatcher, "recipient", "recipient") {
            @Override
            protected Person featureValueOf(Payment actual) {
                return actual.getRecipient();
            }
        };
    }

    public static Matcher<Payment> hasAmount(Matcher<Amount> subMatcher) {
        return new FeatureMatcher<Payment, Amount>(subMatcher, "amount", "amount") {
            @Override
            protected Amount featureValueOf(Payment actual) {
                return actual.getAmount();
            }
        };
    }
}