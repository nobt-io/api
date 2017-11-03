package io.nobt.rest.json.payment;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    public Set<Debt> calculateAccruingDebts() {
        return super.calculateAccruingDebts();
    }

}
