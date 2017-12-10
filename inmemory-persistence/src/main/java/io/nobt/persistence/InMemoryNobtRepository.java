package io.nobt.persistence;

import io.nobt.core.UnknownNobtException;
import io.nobt.core.domain.Nobt;
import io.nobt.core.domain.NobtId;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryNobtRepository implements NobtRepository {

    private static final Map<NobtId, Nobt> nobtDatabase = new HashMap<>();

    @Override
    public NobtId save(Nobt nobt) {

        nobtDatabase.put(nobt.getId(), nobt);

        return nobt.getId();
    }

    @Override
    public Nobt getById(NobtId id) {
        return Optional.ofNullable(nobtDatabase.get(id)).orElseThrow(UnknownNobtException::new);
    }
}
