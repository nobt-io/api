package io.nobt.util;

import org.apache.logging.log4j.util.Supplier;

public final class Lazy<T> implements Supplier<T> {

    private T value;
    private final Supplier<T> factory;

    public Lazy(Supplier<T> factory) {
        this.factory = factory;
    }

    @Override
    public T get() {

        if (value == null) {
            value = factory.get();
        }

        return value;
    }
}
