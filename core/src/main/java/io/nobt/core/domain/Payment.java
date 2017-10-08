package io.nobt.core.domain;

import io.nobt.core.domain.transaction.Debt;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

public class Payment implements CashFlow {

    private final Person sender;
    private final Person recipient;
    private final Amount amount;
    private final String description;
    private final ZonedDateTime addedOn;

    public Payment(Person sender, Person recipient, Amount amount, String description, ZonedDateTime addedOn) {
        this.sender = sender;
        this.recipient = recipient;
        this.amount = amount;
        this.description = description;
        this.addedOn = addedOn;
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

    public ZonedDateTime getAddedOn() {
        return addedOn;
    }

    @Override
    public Set<Debt> calculateAccruingDebts() {
        return Collections.singleton(Debt.debt(recipient, amount, sender));
    }

    @Override
    public ZonedDateTime getCreatedOn() {
        return getAddedOn();
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
                Objects.equals(addedOn, payment.addedOn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sender, recipient, amount, description, addedOn);
    }
}
