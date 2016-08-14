package io.nobt.persistence.mapping;

import io.nobt.core.domain.Expense;
import io.nobt.core.domain.Person;
import io.nobt.core.domain.Share;
import io.nobt.persistence.entity.ExpenseEntity;
import io.nobt.persistence.entity.ShareEntity;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class ExpenseMapper implements DomainModelMapper<ExpenseEntity, Expense> {

    private final DomainModelMapper<ShareEntity, Share> shareMapper;

    public ExpenseMapper(DomainModelMapper<ShareEntity, Share> shareMapper) {
        this.shareMapper = shareMapper;
    }

    @Override
    public Expense mapToDomainModel(ExpenseEntity databaseModel) {

        final List<Share> shares = databaseModel.getShares().stream().map(shareMapper::mapToDomainModel).collect(toList());

        Expense expense = new Expense(databaseModel.getName(), databaseModel.getSplitStrategy(), Person.forName(databaseModel.getDebtee()));
        shares.forEach(expense::addShare);

        return expense;
    }

    @Override
    public ExpenseEntity mapToDatabaseModel(Expense domainModel) {
        return null;
    }
}
