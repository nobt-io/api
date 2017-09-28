package io.nobt.core.domain;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Set;

public class Payment implements CashFlow {

    private final Person sender;
    private final Person recipient;
    private final Amount amount;
    private final String description;
    private final ZonedDateTime addedOn;

    public Payment(Person sender, Person recipient, Amount amount, String description) {
        this.sender = sender;
        this.recipient = recipient;
        this.amount = amount;
        this.description = description;
        this.addedOn = ZonedDateTime.now(ZoneOffset.UTC);
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
}
