package io.nobt.persistence;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.function.Consumer;
import java.util.function.Function;

public class TransactionService {

    private final EntityManager em;

    public TransactionService(EntityManager em) {
        this.em = em;
    }

    public <T> T runInTx(Function<EntityTransaction, T> supplier) {
        final EntityTransaction transaction = em.getTransaction();

        transaction.begin();

        try {
            final T result = supplier.apply(transaction);

            transaction.commit();

            return result;
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }

    public void runInTx(Consumer<EntityTransaction> consumer) {
        final EntityTransaction transaction = em.getTransaction();

        transaction.begin();

        try {
            consumer.accept(transaction);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }
}
