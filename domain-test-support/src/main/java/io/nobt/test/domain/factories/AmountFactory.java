package io.nobt.test.domain.factories;

import io.nobt.core.domain.Amount;

public final class AmountFactory {

    public static Amount amount(double value) {
        return Amount.fromDouble(value);
    }
}
