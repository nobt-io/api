package io.nobt.test.domain.factories;

import io.nobt.core.domain.Amount;
import io.nobt.core.domain.Payment;
import io.nobt.core.domain.Person;

import java.time.LocalDate;
import java.time.ZonedDateTime;

public final class PaymentBuilder {

    private long id;
    private Person sender;
    private Person recipient;
    private Amount amount;
    private String description;
    private ZonedDateTime dateTime;
    private LocalDate date;

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

    public PaymentBuilder createdOn(ZonedDateTime dateTime) {
        this.dateTime = dateTime;
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
                dateTime
        );
    }
}
