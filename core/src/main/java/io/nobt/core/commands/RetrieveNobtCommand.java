package io.nobt.core.commands;

import io.nobt.core.domain.Nobt;
import io.nobt.core.domain.NobtId;
import io.nobt.persistence.NobtRepository;
import io.nobt.persistence.NobtRepositoryCommand;

public final class RetrieveNobtCommand implements NobtRepositoryCommand<Nobt> {

    private final NobtId id;

    public RetrieveNobtCommand(NobtId id) {
        this.id = id;
    }

    @Override
    public Nobt execute(NobtRepository repository) {
        return repository.getById(id);
    }
}
