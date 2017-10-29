package io.nobt.core.domain;

import java.util.Objects;

public final class NobtId {

    private final String value;

    public NobtId(String value) {
        Objects.requireNonNull(value);
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NobtId nobtId = (NobtId) o;

        return value != null ? value.equals(nobtId.value) : nobtId.value == null;
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }

    @Override
    public String toString() {
        return String.format("NobtId{%s}", value);
    }
}
