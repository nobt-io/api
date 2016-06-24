package io.nobt.persistence.dao;

import io.nobt.core.domain.Amount;
import io.nobt.core.domain.Expense;
import io.nobt.core.domain.Nobt;
import io.nobt.core.domain.Person;
import io.nobt.persistence.entity.ExpenseEntity;
import io.nobt.persistence.entity.NobtEntity;

import java.util.Set;
import java.util.stream.Collectors;

public class NobtMapper {

    public Nobt map(NobtEntity entity) {

        final Set<Person> explicitParticipants = entity.getExplicitParticipants().stream().map(Person::forName).collect(Collectors.toSet());

        Nobt nobt = new Nobt(entity.getName(), entity.getUuid(), explicitParticipants);
        entity.getExpenses().stream().map(this::map).forEach(nobt::addExpense);

        return nobt;
    }

    public Expense map(ExpenseEntity entity) {

        Expense expense = new Expense(entity.getName(), Amount.fromBigDecimal(entity.getAmount()), Person.forName(entity.getDebtee()));
        entity.getDebtors().stream().map(Person::forName).forEach(expense::addDebtor);

        return expense;
    }
}
