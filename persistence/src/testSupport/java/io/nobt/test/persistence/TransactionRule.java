package io.nobt.test.persistence;

import org.junit.rules.ExternalResource;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public class TransactionRule extends ExternalResource {

    private final EntityManager entityManager;

    private EntityTransaction transaction;

    public TransactionRule(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    protected void before() throws Throwable {
        transaction = entityManager.getTransaction();
        transaction.begin();
    }

    @Override
    protected void after() {
        transaction.rollback();
    }
}
