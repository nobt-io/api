package io.nobt.persistence.mapping;

import io.nobt.core.domain.Nobt;
import io.nobt.core.domain.NobtId;
import io.nobt.core.domain.Person;
import io.nobt.persistence.entity.NobtEntity;

import java.util.Set;

import static java.util.stream.Collectors.toSet;

public class NobtMapper {

    private final ExpenseMapper expenseMapper;

    public NobtMapper(ExpenseMapper expenseMapper) {
        this.expenseMapper = expenseMapper;
    }

    public Nobt mapToDomain(NobtEntity entity) {

        final Set<Person> explicitParticipants = entity.getExplicitParticipants().stream().map(Person::forName).collect(toSet());

        Nobt nobt = new Nobt(new NobtId(entity.getId()), entity.getName(), explicitParticipants);
        entity.getExpenses().stream().map(expenseMapper::mapToDomain).forEach(nobt::addExpense);

        return nobt;
    }
}
