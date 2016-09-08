package io.nobt.persistence;

import io.nobt.core.UnknownNobtException;
import io.nobt.core.domain.Nobt;
import io.nobt.core.domain.NobtId;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryNobtRepository implements NobtRepository {

    private static final AtomicLong idGenerator = new AtomicLong(1);
    private static final Map<NobtId, Nobt> nobtDatabase = new HashMap<>();

    @Override
    public NobtId save(Nobt nobt) {

        final NobtId id = new NobtId(idGenerator.getAndIncrement());

        final Nobt copy = new Nobt(id, nobt.getName(), nobt.getParticipatingPersons(), nobt.getExpenses());

        nobtDatabase.put(id, copy);

        return id;
    }

    @Override
    public Nobt getById(NobtId id) {
        return Optional.ofNullable(nobtDatabase.get(id)).orElseThrow(() -> new UnknownNobtException(id));
    }
}
