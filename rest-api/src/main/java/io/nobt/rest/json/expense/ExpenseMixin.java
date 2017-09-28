package io.nobt.rest.json.expense;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.nobt.core.domain.ConversionInformation;
import io.nobt.core.domain.Expense;
import io.nobt.core.domain.Person;
import io.nobt.core.domain.Share;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Set;

public abstract class ExpenseMixin extends Expense {

    public ExpenseMixin(Long id, String name, String splitStrategy, Person debtee, ConversionInformation conversionInformation, Set<Share> shares, LocalDate date, ZonedDateTime createdOn) {
        super(id, name, splitStrategy, debtee, conversionInformation, shares, date, createdOn);
    }

    @Override
    @JsonIgnore
    public abstract Set<Person> getParticipants();
}
