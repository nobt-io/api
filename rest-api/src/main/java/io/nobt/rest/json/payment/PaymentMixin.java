package io.nobt.rest.json.payment;

import io.nobt.core.domain.Amount;
import io.nobt.core.domain.ConversionInformation;
import io.nobt.core.domain.Payment;
import io.nobt.core.domain.Person;
import io.nobt.core.domain.debt.Debt;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Set;

public abstract class PaymentMixin extends Payment {

    public PaymentMixin(long id, Person sender, Person recipient, Amount amount, String description, LocalDate date, ConversionInformation conversionInformation, ZonedDateTime createdOn) {
        super(id, sender, recipient, amount, description, date, conversionInformation, createdOn);
    }

    @Override
    public long getId() {
        return super.getId();
    }

    @Override
    public Person getSender() {
        return super.getSender();
    }

    @Override
    public Person getRecipient() {
        return super.getRecipient();
    }

    @Override
    public Amount getAmount() {
        return super.getAmount();
    }

    @Override
    public String getDescription() {
        return super.getDescription();
    }

    @Override
    public LocalDate getDate() {
        return super.getDate();
    }

    @Override
    public ConversionInformation getConversionInformation() {
        return super.getConversionInformation();
    }

    @Override
    public Set<Debt> calculateAccruingDebts() {
        return super.calculateAccruingDebts();
    }

    @Override
    public ZonedDateTime getCreatedOn() {
        return super.getCreatedOn();
    }
}
