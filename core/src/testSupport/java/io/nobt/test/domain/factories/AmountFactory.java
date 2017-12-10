package io.nobt.test.domain.factories;

import io.nobt.core.domain.Amount;

import java.util.Random;

public final class AmountFactory {

    private static final Random amountValueProvider = new Random();

    public static Amount amount(double value) {
        return Amount.fromDouble(value);
    }

    public static Amount randomAmount() {
        return amount(amountValueProvider.nextDouble());
    }
}
