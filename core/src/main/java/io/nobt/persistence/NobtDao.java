package io.nobt.persistence;

import io.nobt.core.domain.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface NobtDao {

	Nobt createNobt(String nobtName, Set<Person> explicitParticipants);

	Expense createExpense(NobtId nobtId, String name, String splitStrategy, Person debtee, List<Share> shares);

	/**
	 * Retrieves a {@link Nobt} instance from the database. Guaranteed to return an object, if it returns.
	 *
	 * @throws io.nobt.core.UnknownNobtException
     */
	Nobt get(NobtId id);

	Optional<Nobt> find(NobtId id);
}
