package io.nobt.core.domain.transaction.combination;

import io.nobt.core.domain.transaction.Transaction;

import java.util.Collection;

public interface CombinationResult {

    boolean hasChanges();

    void applyTo(Collection<Transaction> transactions);

    CombinationResult NotCombinable = new CombinationResult() {

        @Override
        public boolean hasChanges() {
            return false;
        }

        @Override
        public void applyTo(Collection<Transaction> transactions) {

        }
    };

    CombinationResult Illegal = new CombinationResult() {

        @Override
        public boolean hasChanges() {
            return true;
        }

        @Override
        public void applyTo(Collection<Transaction> transactions) {
            throw new IllegalStateException("Illegal combination of transactions!");

        }
    };
}
