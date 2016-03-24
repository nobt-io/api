/**
 * 
 */
package io.nobt.persistence.dao;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import io.nobt.core.domain.Nobt;
import io.nobt.persistence.NobtDao;

/**
 * @author Matthias
 *
 */
public class InMemoryNobtDao implements NobtDao {

	private static final Map<UUID, Nobt> nobtDatabase = new HashMap<>();

	@Override
	public Nobt create(String nobtName) {
		Nobt nobt = new Nobt(nobtName);
		nobtDatabase.put(nobt.getId(), nobt);
		return nobt;
	}

}
