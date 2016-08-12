package io.nobt.core;

import io.nobt.core.domain.NobtId;

public class UnknownNobtException extends RuntimeException {

    private final NobtId id;

    public UnknownNobtException(NobtId id) {
        this.id = id;
    }

    @Override
    public String getMessage() {
        return String.format("Nobt with id '%s' is unknown", id.getId());
    }
}
