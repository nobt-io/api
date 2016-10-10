package io.nobt.persistence;

public interface NobtRepositoryCommand<T> {

    T execute(NobtRepository repository);
}
