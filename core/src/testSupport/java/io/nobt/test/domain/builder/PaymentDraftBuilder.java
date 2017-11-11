package io.nobt.test.domain.builder;

import io.nobt.core.domain.Amount;
import io.nobt.core.domain.ConversionInformation;
import io.nobt.core.domain.PaymentDraft;
import io.nobt.core.domain.Person;

import java.time.LocalDate;

public class PaymentDraftBuilder {

    private Person sender;
    private Person recipient;
    private Amount amount;
    private String description;
    private LocalDate date;
    private ConversionInformation conversionInformation;

    public PaymentDraftBuilder withSender(Person sender) {
        this.sender = sender;
        return this;
    }

    public PaymentDraftBuilder withRecipient(Person recipient) {
        this.recipient = recipient;
        return this;
    }

    public PaymentDraftBuilder withAmount(Amount amount) {
        this.amount = amount;
        return this;
    }

    public PaymentDraftBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public PaymentDraftBuilder withDate(LocalDate date) {
        this.date = date;
        return this;
    }

    public PaymentDraftBuilder withConversionInformation(ConversionInformation conversionInformation) {
        this.conversionInformation = conversionInformation;
        return this;
    }

    public PaymentDraft build() {
        return new PaymentDraft(sender, recipient, amount, description, date, conversionInformation);
    }
}