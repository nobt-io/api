package io.nobt.core.domain;

import java.math.BigDecimal;
import java.util.Objects;

import static java.math.RoundingMode.HALF_UP;

public final class Amount implements Comparable<Amount> {

    public static final Amount ZERO = new Amount(BigDecimal.ZERO);

    private static final int SCALE = 2;

    private final BigDecimal value;

    private Amount(BigDecimal value) {
        this.value = value;
    }

    public static Amount fromBigDecimal(BigDecimal value) {
        return new Amount(value.setScale(SCALE, HALF_UP));
    }

    public static Amount fromDouble(double value) {
        return fromBigDecimal(new BigDecimal(value));
    }

    public BigDecimal getRoundedValue() {
        return value;
    }

    public boolean isPositive() {
        return value.signum() == 1;
    }

    public Amount minus(Amount other) {
        return fromBigDecimal(value.subtract(other.value));
    }

    public Amount multiplyBy(BigDecimal other) {
        return fromBigDecimal(value.multiply(other));
    }

    public Amount plus(Amount other) {
        return fromBigDecimal(value.add(other.value));
    }

    public Amount divideBy(BigDecimal other) {
        return fromBigDecimal(value.divide(other, SCALE, HALF_UP));
    }

    public Amount absolute() {
        return fromBigDecimal(value.abs());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Amount)) return false;
        Amount amount = (Amount) o;
        return Objects.equals(value, amount.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return String.format("%s EURO", value);
    }

    @Override
    public int compareTo(Amount other) {
        return this.value.compareTo(other.value);
    }
}
