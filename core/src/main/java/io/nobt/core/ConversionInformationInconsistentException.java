package io.nobt.core;

import io.nobt.core.domain.ConversionInformation;
import io.nobt.core.domain.CurrencyKey;

public class ConversionInformationInconsistentException extends RuntimeException {

    private final ConversionInformation conversionInformation;
    private final CurrencyKey currencyKey;

    public ConversionInformationInconsistentException(ConversionInformation conversionInformation, CurrencyKey currencyKey) {
        this.currencyKey = currencyKey;
        this.conversionInformation = conversionInformation;
    }

    public CurrencyKey getNobtCurrencyKey() {
        return currencyKey;
    }

    public ConversionInformation getConversionInformation() {
        return conversionInformation;
    }
}
