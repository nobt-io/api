package io.nobt.core.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

import static java.math.RoundingMode.HALF_UP;

public final class Amount {

    public static final Amount ZERO = new Amount(BigDecimal.ZERO);

    private static final RoundingMode ROUNDING_MODE = HALF_UP;
    private static final int INTERNAL_SCALE = 10;
    private static final int EXTERNAL_SCALE = 2;

    private final BigDecimal value;

    private Amount(BigDecimal value) {
        this.value = value;
    }

    public static Amount fromBigDecimal(BigDecimal value) {
        return new Amount(value.setScale(INTERNAL_SCALE, ROUNDING_MODE));
    }

    public static Amount fromDouble(double value) {
        return fromBigDecimal(new BigDecimal(value));
    }

    public BigDecimal getRoundedValue() {
        return value.setScale(EXTERNAL_SCALE, HALF_UP);
    }

    public boolean isPositive() {
        return value.signum() == 1;
    }

    public Amount minus(Amount other) {
        return fromBigDecimal(value.subtract(other.value));
    }

    public Amount plus(Amount other) {
        return fromBigDecimal(value.add(other.value));
    }

    public Amount divide(BigDecimal other) {
        return fromBigDecimal(value.divide(other, INTERNAL_SCALE, HALF_UP));
    }

    public Amount absolute() {
        return fromBigDecimal(value.abs());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Amount)) return false;
        Amount amount = (Amount) o;
        return Objects.equals(getRoundedValue(), amount.getRoundedValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return String.format("%s EURO", getRoundedValue());
    }
}
