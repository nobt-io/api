package io.nobt.core.domain;

import io.nobt.core.PseudoCrypter;
import io.nobt.core.ShortURL;
import io.nobt.core.UnknownNobtException;

import java.util.Objects;

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
        try {
            final long decodedIdentifier = ShortURL.decode(identifier);
            final long decryptedId = PseudoCrypter.pseudoCryptLong(decodedIdentifier);

            return new NobtId(decryptedId);
        } catch (Exception e) {
            throw new UnknownNobtException();
        }
    }

    public String toExternalIdentifier() {

        final long encryptedId = PseudoCrypter.pseudoCryptLong(this.id);
        final String encodedIdentifier = ShortURL.encode(encryptedId);

        return encodedIdentifier;
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
