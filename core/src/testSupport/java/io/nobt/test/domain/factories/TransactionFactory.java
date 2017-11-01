package io.nobt.test.domain.factories;

import io.nobt.core.domain.debt.Debt;

import static io.nobt.test.domain.factories.AmountFactory.randomAmount;
import static io.nobt.test.domain.factories.RandomPersonFactory.randomPerson;

public class TransactionFactory {

    public static Debt randomTransaction() {
        return Debt.debt(randomPerson(), randomAmount(), randomPerson());
    }

}
