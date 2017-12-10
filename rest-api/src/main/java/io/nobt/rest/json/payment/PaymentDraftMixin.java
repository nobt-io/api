package io.nobt.rest.json.payment;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.nobt.core.domain.Amount;
import io.nobt.core.domain.ConversionInformation;
import io.nobt.core.domain.PaymentDraft;
import io.nobt.core.domain.Person;

import java.time.LocalDate;

public abstract class PaymentDraftMixin extends PaymentDraft {

    @JsonCreator
    public PaymentDraftMixin(
            @JsonProperty(value = "sender", required = true) Person sender,
            @JsonProperty(value = "recipient", required = true) Person recipient,
            @JsonProperty(value = "amount", required = true) Amount amount,
            @JsonProperty("description") String description,
            @JsonProperty(value = "date", required = true) LocalDate date,
            @JsonProperty("conversionInformation") ConversionInformation conversionInformation
    ) {
        super(sender, recipient, amount, description, date, conversionInformation);
    }
}
