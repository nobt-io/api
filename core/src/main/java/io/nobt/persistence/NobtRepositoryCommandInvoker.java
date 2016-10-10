package io.nobt.persistence;

public interface NobtRepositoryCommandInvoker {

    <T> T invoke(NobtRepositoryCommand<T> command);

}
