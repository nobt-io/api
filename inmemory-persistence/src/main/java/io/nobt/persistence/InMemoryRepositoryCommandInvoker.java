package io.nobt.persistence;

import java.io.IOException;

public class InMemoryRepositoryCommandInvoker implements NobtRepositoryCommandInvoker {

    private final NobtRepository repository;

    public InMemoryRepositoryCommandInvoker(NobtRepository repository) {
        this.repository = repository;
    }

    @Override
    public <T> T invoke(NobtRepositoryCommand<T> command) {
        return command.execute(repository);
    }

    @Override
    public void close() throws IOException {

    }
}
