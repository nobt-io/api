package io.nobt.core;

import io.nobt.core.domain.ConversionInformation;
import io.nobt.core.domain.CurrencyKey;
import io.nobt.core.domain.Nobt;

public class ConversionInformationInconsistentException extends RuntimeException {

    private final Nobt nobt;
    private final ConversionInformation foreignCurrencyKey;

    public ConversionInformationInconsistentException(Nobt nobt, ConversionInformation foreignCurrencyKey) {
        this.nobt = nobt;
        this.foreignCurrencyKey = foreignCurrencyKey;
    }

    public CurrencyKey getNobtCurrencyKey() {
        return nobt.getCurrencyKey();
    }

    public ConversionInformation getForeignCurrencyKey() {
        return foreignCurrencyKey;
    }
}
