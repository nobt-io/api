package io.nobt.persistence.mapping;

import io.nobt.core.domain.Expense;
import io.nobt.core.domain.Person;
import io.nobt.core.domain.Share;
import io.nobt.persistence.entity.ExpenseEntity;
import io.nobt.persistence.entity.ShareEntity;

import java.util.List;
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

        return new Expense(databaseModel.getName(), databaseModel.getSplitStrategy(), Person.forName(databaseModel.getDebtee()), shares);
    }

    @Override
    public ExpenseEntity mapToDatabaseModel(Expense domainModel) {

        final ExpenseEntity expense = new ExpenseEntity();

        expense.setName(domainModel.getName());
        expense.setDebtee(domainModel.getDebtee().getName());
        expense.setSplitStrategy(domainModel.getSplitStrategy());
        expense.setShares(domainModel.getShares().stream().map(shareMapper::mapToDatabaseModel).collect(toList()));

        return expense;
    }
}
