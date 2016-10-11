package io.nobt.core.domain;

import javax.validation.constraints.Pattern;
import java.util.Objects;

public final class CurrencyKey {

    @Pattern(regexp = "[A-z]{3}")
    private final String key;

    public CurrencyKey(String key) {
        this.key = key.toUpperCase();
    }

    public String getKey() {
        return key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CurrencyKey)) return false;
        CurrencyKey that = (CurrencyKey) o;
        return Objects.equals(key, that.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }

    @Override
    public String toString() {
        return String.format("CurrencyKey{%s}", key);
    }
}
