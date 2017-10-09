package io.nobt.persistence.cashflow.expense;

import io.nobt.core.domain.*;
import io.nobt.persistence.DomainModelMapper;
import io.nobt.persistence.cashflow.CashFlowEntity;
import io.nobt.persistence.share.ShareEntity;

import java.util.Set;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

public class ExpenseMapper implements DomainModelMapper<ExpenseEntity, Expense> {

    private final DomainModelMapper<ShareEntity, Share> shareMapper;

    public ExpenseMapper(DomainModelMapper<ShareEntity, Share> shareMapper) {
        this.shareMapper = shareMapper;
    }

    @Override
    public Expense mapToDomainModel(ExpenseEntity databaseModel) {

        final Set<Share> shares = databaseModel.getShares().stream().map(shareMapper::mapToDomainModel).collect(toSet());

        return new Expense(
                databaseModel.getKey().getId(),
                databaseModel.getName(),
                databaseModel.getSplitStrategy(),
                Person.forName(databaseModel.getDebtee()),
                new ConversionInformation(new CurrencyKey(databaseModel.getCurrency()), databaseModel.getConversionRate()),
                shares,
                databaseModel.getDate(),
                databaseModel.getCreatedOn()
        );
    }

    @Override
    public ExpenseEntity mapToDatabaseModel(Expense domainModel) {

        final ExpenseEntity expense = new ExpenseEntity();

        expense.setKey(new CashFlowEntity.Key(domainModel.getId(), null));
        expense.setName(domainModel.getName());
        expense.setDebtee(domainModel.getDebtee().getName());
        expense.setSplitStrategy(domainModel.getSplitStrategy());
        expense.setShares(domainModel.getShares().stream().map(shareMapper::mapToDatabaseModel).collect(toList()));
        expense.setDate(domainModel.getDate());
        expense.setCreatedOn(domainModel.getCreatedOn());
        expense.setCurrency(domainModel.getConversionInformation().getForeignCurrencyKey().getKey());
        expense.setConversionRate(domainModel.getConversionInformation().getRate());

        return expense;
    }
}
