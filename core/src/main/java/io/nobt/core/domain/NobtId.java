package io.nobt.core.domain;

import java.util.Objects;

import io.nobt.core.ShortURL;

public final class NobtId {

    private final Long id;

    public NobtId(Long id) {
        Objects.requireNonNull(id);
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public static NobtId fromExternalIdentifier(String identifier) {
        return new NobtId(ShortURL.decode(identifier));
    }

    public String toExternalIdentifier() {
        return ShortURL.encode(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NobtId)) {
            return false;
        }
        NobtId nobtId = (NobtId) o;
        return Objects.equals(id, nobtId.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("NobtId[%s]", id);
    }
}
