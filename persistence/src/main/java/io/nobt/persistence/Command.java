package io.nobt.persistence;

@FunctionalInterface
public interface Command<X> {

    X run();
}
