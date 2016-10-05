package io.nobt.rest.json.expense;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.nobt.core.domain.CurrencyKey;

import java.math.BigDecimal;

public abstract class ConversionInformationMixin {

    @JsonCreator
    public ConversionInformationMixin(@JsonProperty("foreignCurrency") CurrencyKey foreignCurrencyKey, @JsonProperty("rate") BigDecimal rate) {

    }

    @JsonProperty("foreignCurrency")
    public abstract CurrencyKey getForeignCurrencyKey();

    @JsonProperty("rate")
    public abstract BigDecimal getRate();
}
