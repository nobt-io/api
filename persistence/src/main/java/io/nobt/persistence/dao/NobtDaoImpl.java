package io.nobt.persistence.dao;

import io.nobt.core.UnknownNobtException;
import io.nobt.core.domain.Expense;
import io.nobt.core.domain.Nobt;
import io.nobt.core.domain.Person;
import io.nobt.persistence.NobtDao;
import io.nobt.persistence.entity.ExpenseEntity;
import io.nobt.persistence.entity.NobtEntity;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Matthias
 *
 */
public class NobtDaoImpl implements NobtDao {

	private EntityManager em;

	private NobtMapper nobtMapper;

	public NobtDaoImpl(EntityManager em, NobtMapper nobtMapper) {
		this.em = em;
		this.nobtMapper = nobtMapper;
	}

	@Override
	public Nobt create(String nobtName, Set<Person> explicitParticipants) {
		em.getTransaction().begin();

		final Set<String> participantsAsStringList = explicitParticipants.stream().map(Person::getName).collect(Collectors.toSet());

		NobtEntity nobt = new NobtEntity(nobtName, UUID.randomUUID(), participantsAsStringList);

		em.persist(nobt);
		em.getTransaction().commit();

		return nobtMapper.map(nobt);
	}

	@Override
	public Expense createExpense(UUID nobtId, String name, BigDecimal amount, Person debtee, Set<Person> debtors) {

		em.getTransaction().begin();

		NobtEntity nobt = findNobt(nobtId).orElseThrow( () -> new UnknownNobtException(nobtId));

		ExpenseEntity expense = new ExpenseEntity(name, amount, debtee.getName());
		debtors.stream().map(Person::getName).forEach(expense::addDebtor);

		nobt.addExpense(expense);

		em.merge(nobt);
		em.getTransaction().commit();

		return nobtMapper.map(expense);
	}

	@Override
	public Nobt get(UUID nobtId) {
		return find(nobtId).orElseThrow( () -> new UnknownNobtException(nobtId) );
	}

	@Override
	public Optional<Nobt> find(UUID nobtId) {
		final Optional<NobtEntity> nobt = findNobt(nobtId);

		return nobt.map(nobtMapper::map);
	}

	private Optional<NobtEntity> findNobt(UUID nobtId) {
		TypedQuery<NobtEntity> query = em
				.createQuery("from NobtEntity where uuid = :uuid", NobtEntity.class)
				.setParameter("uuid", nobtId);
		return query.getResultList().stream().findFirst();
	}

}
