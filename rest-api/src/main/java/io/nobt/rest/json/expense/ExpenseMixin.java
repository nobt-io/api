package io.nobt.rest.json.expense;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.nobt.core.domain.Person;
import io.nobt.core.domain.transaction.Transaction;

import java.util.List;

public abstract class ExpenseMixin {

    @JsonIgnore
    public abstract List<Transaction> getTransactions();

    @JsonIgnore
    public abstract List<Person> getParticipants();
}
