package io.nobt.profiles.spi;

public class ActiveProfileEvaluatorLoaderException extends RuntimeException {

    public ActiveProfileEvaluatorLoaderException() {
    }

    public ActiveProfileEvaluatorLoaderException(String message) {
        super(message);
    }

    public ActiveProfileEvaluatorLoaderException(String message, Throwable cause) {
        super(message, cause);
    }

    public ActiveProfileEvaluatorLoaderException(Throwable cause) {
        super(cause);
    }
}
