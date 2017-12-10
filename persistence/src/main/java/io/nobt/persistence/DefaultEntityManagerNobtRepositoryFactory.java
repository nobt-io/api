package io.nobt.persistence;

import io.nobt.persistence.cashflow.expense.ExpenseMapper;
import io.nobt.persistence.cashflow.payment.PaymentMapper;
import io.nobt.persistence.mapping.EntityManagerNobtDatabaseIdResolver;
import io.nobt.persistence.nobt.NobtMapper;
import io.nobt.persistence.share.ShareMapper;

import javax.persistence.EntityManager;

public class DefaultEntityManagerNobtRepositoryFactory implements EntityManagerNobtRepositoryFactory {

    @Override
    public EntityManagerNobtRepository create(EntityManager entityManager) {
        return new EntityManagerNobtRepository(
                entityManager,
                new NobtMapper(
                        new EntityManagerNobtDatabaseIdResolver(entityManager),
                        new ExpenseMapper(
                                new ShareMapper()
                        ),
                        new PaymentMapper()
                )
        );
    }
}
