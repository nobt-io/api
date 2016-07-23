package io.nobt.persistence.dao;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import io.nobt.core.UnknownNobtException;
import io.nobt.core.domain.Expense;
import io.nobt.core.domain.Nobt;
import io.nobt.core.domain.NobtId;
import io.nobt.core.domain.Person;
import io.nobt.persistence.NobtDao;
import io.nobt.persistence.entity.ExpenseEntity;
import io.nobt.persistence.entity.NobtEntity;

/**
 * @author Matthias
 *
 */
public class NobtDaoImpl implements NobtDao {

	private final EntityManager em;
	private final NobtMapper nobtMapper;

    public NobtDaoImpl(EntityManager em, NobtMapper nobtMapper) {
		this.em = em;
		this.nobtMapper = nobtMapper;
    }

	@Override
	public Nobt create(String nobtName, Set<Person> explicitParticipants) {
		em.getTransaction().begin();

		final Set<String> participantsAsStringList = explicitParticipants.stream().map(Person::getName).collect(Collectors.toSet());

		NobtEntity nobt = new NobtEntity(nobtName, participantsAsStringList);

		em.persist(nobt);
		em.getTransaction().commit();

		return nobtMapper.mapNobt(nobt);
	}

	@Override
	public Expense createExpense(NobtId nobtId, String name, BigDecimal amount, Person debtee, Set<Person> debtors) {

		em.getTransaction().begin();

        NobtEntity nobt = findNobtEntity(nobtId).orElseThrow( () -> new UnknownNobtException(nobtId));

		ExpenseEntity expense = new ExpenseEntity(name, amount, debtee.getName());
		debtors.stream().map(Person::getName).forEach(expense::addDebtor);

		nobt.addExpense(expense);

		em.merge(nobt);
		em.getTransaction().commit();

		return nobtMapper.mapExpense(expense);
	}

	@Override
	public Nobt get(NobtId id) {
		return find(id).orElseThrow( () -> new UnknownNobtException(id) );
	}

	@Override
	public Optional<Nobt> find(NobtId nobtId) {

        final Optional<NobtEntity> nobt = findNobtEntity(nobtId);

        return nobt.map(nobtMapper::mapNobt);
	}

    private Optional<NobtEntity> findNobtEntity(NobtId nobtId) {
        return Optional.ofNullable(em.find(NobtEntity.class, nobtId.getId()));
    }

}
