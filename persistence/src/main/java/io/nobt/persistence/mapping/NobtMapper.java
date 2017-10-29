package io.nobt.persistence.mapping;

import io.nobt.core.domain.*;
import io.nobt.persistence.entity.ExpenseEntity;
import io.nobt.persistence.entity.NobtEntity;

import java.util.Set;

import static java.util.stream.Collectors.toSet;

public class NobtMapper implements DomainModelMapper<NobtEntity, Nobt> {

    private final NobtDatabaseIdResolver nobtDatabaseIdResolver;
    private final DomainModelMapper<ExpenseEntity, Expense> expenseMapper;

    public NobtMapper(NobtDatabaseIdResolver nobtDatabaseIdResolver, DomainModelMapper<ExpenseEntity, Expense> expenseMapper) {
        this.nobtDatabaseIdResolver = nobtDatabaseIdResolver;
        this.expenseMapper = expenseMapper;
    }

    @Override
    public Nobt mapToDomainModel(NobtEntity databaseModel) {

        final Set<Person> explicitParticipants = databaseModel.getExplicitParticipants().stream().map(Person::forName).collect(toSet());
        final Set<Expense> expenses = databaseModel.getExpenses().stream().map(expenseMapper::mapToDomainModel).collect(toSet());

        return new Nobt(
                new NobtId(databaseModel.getExternalId()),
                new CurrencyKey(databaseModel.getCurrency()),
                databaseModel.getName(),
                explicitParticipants,
                expenses,
                databaseModel.getCreatedOn(),
                databaseModel.getOptimizer()
        );
    }

    @Override
    public NobtEntity mapToDatabaseModel(Nobt domainModel) {

        final NobtEntity nobtEntity = new NobtEntity();

        final NobtId nobtId = domainModel.getId();

        nobtDatabaseIdResolver
                .resolveDatabaseId(nobtId.getValue())
                .ifPresent(nobtEntity::setId);

        nobtEntity.setExternalId(nobtId.getValue());
        nobtEntity.setName(domainModel.getName());
        nobtEntity.setCurrency(domainModel.getCurrencyKey().getKey());
        nobtEntity.setCreatedOn(domainModel.getCreatedOn());
        nobtEntity.setOptimizer(domainModel.getOptimizer());

        domainModel.getParticipatingPersons().stream().map(Person::getName).forEach(nobtEntity::addExplicitParticipant);
        domainModel.getExpenses().stream().map(expenseMapper::mapToDatabaseModel).forEach(nobtEntity::addExpense);

        return nobtEntity;
    }
}
