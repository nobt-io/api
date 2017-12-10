package io.nobt.test.domain.matchers;

import io.nobt.core.domain.ConversionInformation;
import io.nobt.core.domain.CurrencyKey;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;

import java.math.BigDecimal;

public final class ConversionInformationMatchers {

    private ConversionInformationMatchers() {
    }

    public static Matcher<ConversionInformation> isValid(CurrencyKey currencyKey, Matcher<Boolean> subMatcher) {
        return new FeatureMatcher<ConversionInformation, Boolean>(subMatcher, "isValid(" + currencyKey + ")", "isValid(" + currencyKey + ")") {
            @Override
            protected Boolean featureValueOf(ConversionInformation actual) {
                return actual.isValid(currencyKey);
            }
        };
    }

    public static Matcher<ConversionInformation> hasRate(Matcher<BigDecimal> subMatcher) {
        return new FeatureMatcher<ConversionInformation, BigDecimal>(subMatcher, "rate", "rate") {
            @Override
            protected BigDecimal featureValueOf(ConversionInformation actual) {
                return actual.getRate();
            }
        };
    }
}
