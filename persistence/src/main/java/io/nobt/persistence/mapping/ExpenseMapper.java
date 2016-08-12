package io.nobt.persistence.mapping;

import io.nobt.core.domain.Expense;
import io.nobt.core.domain.Person;
import io.nobt.core.domain.Share;
import io.nobt.persistence.entity.ExpenseEntity;

import java.util.Set;

public class ExpenseMapper {

    private final ShareMapper shareMapper;

    public ExpenseMapper(ShareMapper shareMapper) {
        this.shareMapper = shareMapper;
    }

    public Expense mapToDomain(ExpenseEntity entity) {

        final byte[] sharesAsBytes = entity.getShares();

        final Set<Share> shares = shareMapper.mapToShareSet(sharesAsBytes);

        Expense expense = new Expense(entity.getName(), entity.getSplitStrategy(), Person.forName(entity.getDebtee()));
        shares.forEach(expense::addShare);

        return expense;
    }
}
