package io.nobt.core;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public final class Amount {

    private final BigDecimal value;

    private Amount(BigDecimal value) {
        this.value = value;
    }

    public static Amount fromBigDecimal(BigDecimal value) {
        return new Amount(value.setScale(10, RoundingMode.HALF_UP));
    }

    public static Amount fromDouble(double value) {
        return fromBigDecimal(new BigDecimal(value));
    }

    public BigDecimal getRoundedValue() {
        return value.setScale(2, RoundingMode.HALF_UP);
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
		return String.format("%s EURO", getRoundedValue());
	}
}
