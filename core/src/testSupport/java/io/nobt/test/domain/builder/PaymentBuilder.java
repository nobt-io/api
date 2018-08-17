package io.nobt.test.domain.builder;

import io.nobt.core.domain.Amount;
import io.nobt.core.domain.ConversionInformation;
import io.nobt.core.domain.Payment;
import io.nobt.core.domain.Person;

import java.time.Instant;
import java.time.LocalDate;

public final class PaymentBuilder {

    private long id;
    private Person sender;
    private Person recipient;
    private Amount amount;
    private String description;
    private Instant dateTime;
    private LocalDate date;
    private ConversionInformation conversionInformation;

    public PaymentBuilder withId(long id) {
        this.id = id;
        return this;
    }

    public PaymentBuilder withSender(Person sender) {
        this.sender = sender;
        return this;
    }

    public PaymentBuilder withRecipient(Person recipient) {
        this.recipient = recipient;
        return this;
    }

    public PaymentBuilder withAmount(Amount amount) {
        this.amount = amount;
        return this;
    }

    public PaymentBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public PaymentBuilder happendOn(LocalDate date) {
        this.date = date;
        return this;
    }

    public PaymentBuilder createdOn(Instant dateTime) {
        this.dateTime = dateTime;
        return this;
    }

    public PaymentBuilder withConversionInformation(ConversionInformation conversionInformation) {
        this.conversionInformation = conversionInformation;
        return this;
    }

    public Payment build() {
        return new Payment(
                id,
                sender,
                recipient,
                amount,
                description,
                date,
                conversionInformation,
                dateTime
        );
    }
}
