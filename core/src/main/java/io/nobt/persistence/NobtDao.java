package io.nobt.persistence;

import io.nobt.core.domain.*;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface NobtDao {

	Nobt create(String nobtName, Set<Person> explicitParticipants);

	Expense createExpense(NobtId nobtId, String name, String splitStrategy, Person debtee, Set<Share> shares);

	@Deprecated
	Expense createExpense(NobtId nobtId, String name, BigDecimal amount, Person debtee, Set<Person> debtors);

	/**
	 * Retrieves a {@link Nobt} instance from the database. Guaranteed to return an object, if it returns.
	 *
	 * @throws io.nobt.core.UnknownNobtException
     */
	Nobt get(NobtId id);

	Optional<Nobt> find(NobtId id);
}
