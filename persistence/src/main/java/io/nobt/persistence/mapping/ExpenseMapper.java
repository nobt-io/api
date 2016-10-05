package io.nobt.persistence.mapping;

import io.nobt.core.domain.Expense;
import io.nobt.core.domain.Person;
import io.nobt.core.domain.Share;
import io.nobt.persistence.entity.ExpenseEntity;
import io.nobt.persistence.entity.ShareEntity;

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
                databaseModel.getId(),
                databaseModel.getName(),
                databaseModel.getSplitStrategy(),
                Person.forName(databaseModel.getDebtee()),
                shares,
                databaseModel.getDate(),
                databaseModel.getCreatedOn()
        );
    }

    @Override
    public ExpenseEntity mapToDatabaseModel(Expense domainModel) {

        final ExpenseEntity expense = new ExpenseEntity();

        if (domainModel.getId() != null) {
            expense.setId(domainModel.getId());
        }

        expense.setName(domainModel.getName());
        expense.setDebtee(domainModel.getDebtee().getName());
        expense.setSplitStrategy(domainModel.getSplitStrategy());
        expense.setShares(domainModel.getShares().stream().map(shareMapper::mapToDatabaseModel).collect(toList()));
        expense.setDate(domainModel.getDate());
        expense.setCreatedOn(domainModel.getCreatedOn());

        return expense;
    }
}
