package io.nobt.core.commands;

import io.nobt.core.domain.Nobt;
import io.nobt.core.domain.NobtId;
import io.nobt.persistence.NobtRepository;
import io.nobt.persistence.NobtRepositoryCommand;

public abstract class AbstractNobtModifyingCommand<T> implements NobtRepositoryCommand<T> {

    private final NobtId id;

    protected AbstractNobtModifyingCommand(NobtId id) {
        this.id = id;
    }

    @Override
    public T execute(NobtRepository repository) {

        final Nobt nobt = repository.getById(id);

        modify(nobt);

        repository.save(nobt);

        return null;
    }

    protected abstract void modify(Nobt nobt);
}
