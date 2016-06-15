package io.nobt.core;

import java.util.UUID;

public class UnknownNobtException extends RuntimeException {

    private final UUID id;

    public UnknownNobtException(UUID id) {
        this.id = id;
    }

    public UnknownNobtException(String message, UUID id) {
        super(message);
        this.id = id;
    }

    public UnknownNobtException(String message, Throwable cause, UUID id) {
        super(message, cause);
        this.id = id;
    }

    public UnknownNobtException(Throwable cause, UUID id) {
        super(cause);
        this.id = id;
    }

    @Override
    public String getMessage() {
        return String.format("Nobt with id %s is unknown", id);
    }
}
