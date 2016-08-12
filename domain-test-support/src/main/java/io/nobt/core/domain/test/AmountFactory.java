package io.nobt.core.domain.test;

import io.nobt.core.domain.Amount;

public class AmountFactory {

    public static Amount amount(double value) {
        return Amount.fromDouble(value);
    }
}
