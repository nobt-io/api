package io.nobt.persistence;

import io.nobt.core.domain.Nobt;

public interface NobtDao {

	Nobt create(String nobtName);

}
