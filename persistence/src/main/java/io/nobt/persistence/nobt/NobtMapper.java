package io.nobt.persistence.nobt;

import io.nobt.core.domain.*;
import io.nobt.persistence.DomainModelMapper;
import io.nobt.persistence.cashflow.expense.ExpenseEntity;
import io.nobt.persistence.cashflow.payment.PaymentEntity;
import io.nobt.persistence.mapping.NobtDatabaseIdResolver;

import java.util.Set;

import static java.util.stream.Collectors.toSet;

public class NobtMapper implements DomainModelMapper<NobtEntity, Nobt> {

    private final NobtDatabaseIdResolver nobtDatabaseIdResolver;
    private final DomainModelMapper<ExpenseEntity, Expense> expenseMapper;
    private final DomainModelMapper<PaymentEntity, Payment> paymentMapper;

    public NobtMapper(NobtDatabaseIdResolver nobtDatabaseIdResolver,
                      DomainModelMapper<ExpenseEntity, Expense> expenseMapper,
                      DomainModelMapper<PaymentEntity, Payment> paymentMapper) {
        this.nobtDatabaseIdResolver = nobtDatabaseIdResolver;
        this.expenseMapper = expenseMapper;
        this.paymentMapper = paymentMapper;
    }

    @Override
    public Nobt mapToDomainModel(NobtEntity databaseModel) {

        final Set<Person> explicitParticipants = databaseModel.getExplicitParticipants();
        final Set<Expense> expenses = databaseModel.getExpenses().stream()
                .filter(ExpenseEntity::isNotDeleted)
                .map(expenseMapper::mapToDomainModel)
                .collect(toSet());
        final Set<Expense> deletedExpenses = databaseModel.getExpenses().stream()
                .filter(ExpenseEntity::isDeleted)
                .map(expenseMapper::mapToDomainModel)
                .collect(toSet());
        final Set<Payment> payments = databaseModel.getPayments().stream()
                .map(paymentMapper::mapToDomainModel)
                .collect(toSet());

        return new Nobt(
                new NobtId(databaseModel.getExternalId()),
                new CurrencyKey(databaseModel.getCurrency()),
                databaseModel.getName(),
                explicitParticipants,
                expenses,
                deletedExpenses,
                payments,
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
        nobtEntity.setExplicitParticipant(domainModel.getParticipatingPersons());

        domainModel.getExpenses().stream().map(expenseMapper::mapToDatabaseModel).forEach(nobtEntity::addExpense);
        domainModel.getDeletedExpenses().stream()
                .map(expenseMapper::mapToDatabaseModel)
                .peek(e -> e.setDeleted(true))
                .forEach(nobtEntity::addExpense);
        domainModel.getPayments().stream().map(paymentMapper::mapToDatabaseModel).forEach(nobtEntity::addPayment);

        return nobtEntity;
    }
}
