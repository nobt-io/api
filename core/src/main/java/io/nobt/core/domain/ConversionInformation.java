package io.nobt.core.domain;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public final class ConversionInformation {

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

    public static ConversionInformation sameCurrencyAs(Nobt nobt) {
        return new ConversionInformation(
                nobt.getCurrencyKey(),
                BigDecimal.ONE
        );
    }

    public boolean hasDefaultRate() {
        return rate.equals(BigDecimal.ONE);
    }

    public CurrencyKey getForeignCurrencyKey() {
        return foreignCurrencyKey;
    }

    public BigDecimal getRate() {
        return rate;
    }
}
