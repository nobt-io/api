package io.nobt.rest.json.expense;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.nobt.core.domain.ConversionInformation;
import io.nobt.core.domain.ExpenseDraft;
import io.nobt.core.domain.Person;
import io.nobt.core.domain.Share;

import java.time.LocalDate;
import java.util.Set;

public abstract class ExpenseDraftMixin extends ExpenseDraft {

    @JsonCreator
    public ExpenseDraftMixin(
            @JsonProperty(value = "name", required = true) String name,
            @JsonProperty(value = "splitStrategy", required = true) String splitStrategy,
            @JsonProperty(value = "debtee", required = true) Person debtee,
            @JsonProperty(value = "shares", required = true) Set<Share> shares,
            @JsonProperty(value = "date", required = true) LocalDate date,
            @JsonProperty(value = "conversionInformation", required = true) ConversionInformation conversionInformation) {
        super(name, splitStrategy, debtee, shares, date, conversionInformation);
    }
}
