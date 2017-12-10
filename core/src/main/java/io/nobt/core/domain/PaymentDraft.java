package io.nobt.core.domain;

import io.nobt.core.validation.Positive;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Optional;

public class PaymentDraft {

    @Valid
    @NotNull
    private final Person sender;

    @Valid
    @NotNull
    private final Person recipient;

    @Positive
    private final Amount amount;

    private final String description;

    @NotNull
    private final LocalDate date;

    @Valid
    private final ConversionInformation conversionInformation;

    public PaymentDraft(Person sender, Person recipient, Amount amount, String description, LocalDate date, ConversionInformation conversionInformation) {
        this.sender = sender;
        this.recipient = recipient;
        this.amount = amount;
        this.description = description;
        this.date = date;
        this.conversionInformation = conversionInformation;
    }

    public Person getSender() {
        return sender;
    }

    public Person getRecipient() {
        return recipient;
    }

    public Amount getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getDate() {
        return date;
    }

    public Optional<ConversionInformation> getConversionInformation() {
        return Optional.ofNullable(conversionInformation);
    }
}
