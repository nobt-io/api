/**
 * 
 */
package io.nobt.persistence.dao;

import io.nobt.core.domain.Expense;
import io.nobt.core.domain.Nobt;
import io.nobt.core.domain.Person;
import io.nobt.persistence.NobtDao;
import io.nobt.persistence.entity.ExpenseEntity;
import io.nobt.persistence.entity.NobtEntity;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

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
	public Nobt create(String nobtName) {
		em.getTransaction().begin();

		NobtEntity nobt = new NobtEntity(nobtName, UUID.randomUUID());

		em.persist(nobt);
		em.getTransaction().commit();

		return nobtMapper.map(nobt);
	}

	@Override
	public Expense createExpense(UUID nobtId, String name, BigDecimal amount, Person debtee, Set<Person> debtors) {

		em.getTransaction().begin();

		NobtEntity nobt = findNobt(nobtId);

		ExpenseEntity expense = new ExpenseEntity(name, amount, debtee.getName());
		debtors.stream().map(Person::getName).forEach(expense::addDebtor);

		nobt.addExpense(expense);

		em.merge(nobt);
		em.getTransaction().commit();

		return nobtMapper.map(expense);
	}

	// TODO, define as optional
	@Override
	public Nobt find(UUID nobtId) {
		return nobtMapper.map(findNobt(nobtId));
	}

	private NobtEntity findNobt(UUID nobtId) {
		TypedQuery<NobtEntity> query = em.createQuery("from NobtEntity where uuid = :uuid", NobtEntity.class)
				.setParameter("uuid", nobtId);
		return query.getSingleResult();
	}

}
