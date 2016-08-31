package io.nobt.persistence;

import io.nobt.core.domain.Nobt;
import io.nobt.core.domain.NobtId;

public interface NobtRepository {

    NobtId save(Nobt nobt);

	Nobt getById(NobtId id);
}
