package io.nobt.persistence;

import io.nobt.core.domain.Nobt;
import io.nobt.persistence.cashflow.expense.ExpenseMapper;
import io.nobt.persistence.cashflow.payment.PaymentMapper;
import io.nobt.persistence.nobt.NobtEntity;
import io.nobt.persistence.nobt.NobtMapper;
import io.nobt.persistence.share.ShareMapper;

import javax.persistence.EntityManager;

public class DefaultEntityManagerNobtRepositoryFactory implements EntityManagerNobtRepositoryFactory {

    private final DomainModelMapper<NobtEntity, Nobt> domainModelMapper;

    public DefaultEntityManagerNobtRepositoryFactory() {
        this(new NobtMapper(new ExpenseMapper(new ShareMapper()), new PaymentMapper()));
    }

    public DefaultEntityManagerNobtRepositoryFactory(DomainModelMapper<NobtEntity, Nobt> domainModelMapper) {
        this.domainModelMapper = domainModelMapper;
    }

    @Override
    public EntityManagerNobtRepository create(EntityManager entityManager) {
        return new EntityManagerNobtRepository(entityManager, domainModelMapper);
    }
}
