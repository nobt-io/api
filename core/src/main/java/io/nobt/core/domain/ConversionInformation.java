package io.nobt.core.domain;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public final class ConversionInformation {

    private static final BigDecimal DEFAULT_RATE = BigDecimal.ONE;

    @NotNull
    @Valid
    private final CurrencyKey foreignCurrencyKey;

    @NotNull
    @Min(0)
    private final BigDecimal rate;

    public ConversionInformation(CurrencyKey foreignCurrencyKey, BigDecimal rate) {
        this.foreignCurrencyKey = foreignCurrencyKey;
        this.rate = rate;
    }

    public static ConversionInformation defaultConversionInformation(CurrencyKey currencyKey) {
        return new ConversionInformation(currencyKey, DEFAULT_RATE);
    }

    public boolean isConsistent(CurrencyKey nobtCurrency) {
        return hasDefaultRate() || !isSameCurrency(nobtCurrency);
    }

    public CurrencyKey getForeignCurrencyKey() {
        return foreignCurrencyKey;
    }

    public BigDecimal getRate() {
        return rate;
    }

    private boolean isSameCurrency(CurrencyKey nobtCurrency) {
        return nobtCurrency.equals(foreignCurrencyKey);
    }

    private boolean hasDefaultRate() {
        return DEFAULT_RATE.equals(rate);
    }
}
