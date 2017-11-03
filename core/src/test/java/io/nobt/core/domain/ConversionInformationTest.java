package io.nobt.core.domain;

import org.junit.Test;

import java.math.BigDecimal;

import static io.nobt.core.domain.ConversionInformation.defaultConversionInformation;
import static io.nobt.test.domain.factories.CurrencyKeysProvider.EUR;
import static io.nobt.test.domain.factories.CurrencyKeysProvider.USD;
import static io.nobt.test.domain.matchers.ConversionInformationMatchers.hasRate;
import static io.nobt.test.domain.matchers.ConversionInformationMatchers.isValid;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ConversionInformationTest {

    @Test
    public void givenTheDefaultConversionInformation_whenCheckedForValidity_thenShouldReportTrue() throws Exception {

        final ConversionInformation conversionInformation = defaultConversionInformation(EUR);

        assertThat(conversionInformation, isValid(EUR, is(true)));
    }

    @Test
    public void givenAConversionInformationWithOtherRateThanDefault_whenCheckedForValidity_thenShouldReportTrue() throws Exception {

        final ConversionInformation conversionInformation = new ConversionInformation(USD, BigDecimal.valueOf(1.2));

        assertThat(conversionInformation, isValid(EUR, is(true)));
    }

    @Test
    public void defaultRateShouldBeOne() throws Exception {
        assertThat(defaultConversionInformation(EUR), hasRate(equalTo(BigDecimal.ONE)));
    }
}