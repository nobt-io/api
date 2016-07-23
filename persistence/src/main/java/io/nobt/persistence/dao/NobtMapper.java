package io.nobt.persistence.dao;

import java.util.Set;
import java.util.stream.Collectors;

import io.nobt.core.domain.Amount;
import io.nobt.core.domain.Expense;
import io.nobt.core.domain.Nobt;
import io.nobt.core.domain.NobtId;
import io.nobt.core.domain.Person;
import io.nobt.persistence.entity.ExpenseEntity;
import io.nobt.persistence.entity.NobtEntity;

public class NobtMapper {

    public Nobt mapNobt(NobtEntity entity) {

        final Set<Person> explicitParticipants = entity.getExplicitParticipants().stream().map(Person::forName).collect(Collectors.toSet());

        Nobt nobt = new Nobt(new NobtId(entity.getId()), entity.getName(), explicitParticipants);
        entity.getExpenses().stream().map(this::mapExpense).forEach(nobt::addExpense);

        return nobt;
    }

    public Expense mapExpense(ExpenseEntity entity) {

        Expense expense = new Expense(entity.getName(), Amount.fromBigDecimal(entity.getAmount()), Person.forName(entity.getDebtee()));
        entity.getDebtors().stream().map(Person::forName).forEach(expense::addDebtor);

        return expense;
    }
}
