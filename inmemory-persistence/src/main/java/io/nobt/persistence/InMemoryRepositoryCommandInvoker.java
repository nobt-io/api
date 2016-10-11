package io.nobt.persistence;

public class InMemoryRepositoryCommandInvoker implements NobtRepositoryCommandInvoker {

    private final NobtRepository repository;

    public InMemoryRepositoryCommandInvoker(NobtRepository repository) {
        this.repository = repository;
    }

    @Override
    public <T> T invoke(NobtRepositoryCommand<T> command) {
        return command.execute(repository);
    }
}
