package io.nobt.core;

import io.nobt.core.domain.NobtId;

public class UnknownNobtException extends RuntimeException {

    private final NobtId id;

    public UnknownNobtException(NobtId id) {
        this.id = id;
    }

    public UnknownNobtException(String message, NobtId id) {
        super(message);
        this.id = id;
    }

    public UnknownNobtException(String message, Throwable cause, NobtId id) {
        super(message, cause);
        this.id = id;
    }

    public UnknownNobtException(Throwable cause, NobtId id) {
        super(cause);
        this.id = id;
    }

    @Override
    public String getMessage() {
        return String.format("Nobt with id '%s' is unknown", id);
    }
}
