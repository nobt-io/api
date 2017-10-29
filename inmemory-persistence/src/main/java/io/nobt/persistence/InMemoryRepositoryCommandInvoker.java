package io.nobt.persistence;

public class InMemoryRepositoryCommandInvoker implements NobtRepositoryCommandInvoker {

    private final InMemoryNobtRepository repository;

    public InMemoryRepositoryCommandInvoker(InMemoryNobtRepository repository) {
        this.repository = repository;
    }

    @Override
    public <T> T invoke(NobtRepositoryCommand<T> command) {
        return command.execute(repository);
    }
}
