package io.nobt.core.domain;

import io.nobt.core.ConversionInformationInconsistentException;
import io.nobt.core.domain.debt.Debt;
import io.nobt.util.Sets;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

import static io.nobt.core.domain.ConversionInformation.defaultConversionInformation;

public class Payment implements CashFlow {

    private final long id;
    private final Person sender;
    private final Person recipient;
    private final Amount amount;
    private final String description;
    private final LocalDate date;
    private final ConversionInformation conversionInformation;
    private final Instant createdOn;

    public Payment(long id, Person sender, Person recipient, Amount amount, String description, LocalDate date, ConversionInformation conversionInformation, Instant createdOn) {
        this.id = id;
        this.sender = sender;
        this.recipient = recipient;
        this.amount = amount;
        this.description = description;
        this.date = date;
        this.conversionInformation = conversionInformation;

        this.createdOn = createdOn;
    }

    public static Payment fromDraft(long id, CurrencyKey nobtCurrency, PaymentDraft draft) {

        final ConversionInformation conversionInformation = draft
                .getConversionInformation()
                .orElse(defaultConversionInformation(nobtCurrency));

        if (!conversionInformation.isValid(nobtCurrency)) {
            throw new ConversionInformationInconsistentException(conversionInformation, nobtCurrency);
        }

        return new Payment(id, draft.getSender(), draft.getRecipient(), draft.getAmount(), draft.getDescription(), draft.getDate(), conversionInformation, Instant.now(Clock.systemUTC()));
    }

    @Override
    public long getId() {
        return id;
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

    public ConversionInformation getConversionInformation() {
        return conversionInformation;
    }

    @Override
    public Set<Debt> calculateAccruingDebts() {
        return Collections.singleton(Debt.debt(recipient, amount, sender));
    }

    @Override
    public Instant getCreatedOn() {
        return createdOn;
    }

    @Override
    public Set<Person> getParticipants() {
        return Sets.newHashSet(getSender(), getRecipient());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Payment payment = (Payment) o;
        return Objects.equals(sender, payment.sender) &&
                Objects.equals(recipient, payment.recipient) &&
                Objects.equals(amount, payment.amount) &&
                Objects.equals(description, payment.description) &&
                Objects.equals(createdOn, payment.createdOn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sender, recipient, amount, description, createdOn);
    }
}
