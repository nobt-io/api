package io.nobt.rest.json.expense;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.nobt.core.domain.DeletedExpense;
import io.nobt.core.domain.Expense;
import io.nobt.core.domain.Person;

import java.time.Instant;
import java.util.Set;

public abstract class DeletedExpenseMixin extends DeletedExpense {

    public DeletedExpenseMixin(Expense originalExpense, Instant deletedOn) {
        super(originalExpense, deletedOn);
    }

    @Override
    @JsonIgnore
    public abstract Set<Person> getParticipants();

    @Override
    @JsonIgnore
    public Expense getOriginalExpense() {
        return super.getOriginalExpense();
    }
}
