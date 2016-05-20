/**
 * 
 */
package io.nobt.persistence.dao;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import io.nobt.core.domain.Expense;
import io.nobt.core.domain.Nobt;
import io.nobt.core.domain.Person;
import io.nobt.persistence.NobtDao;
import io.nobt.persistence.entity.ExpenseEntity;
import io.nobt.persistence.entity.NobtEntity;
import io.nobt.persistence.entity.PersonEntity;

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

		ExpenseEntity expense = new ExpenseEntity(name, amount, new PersonEntity(debtee.getName()));
		expense.getDebtors()
				.addAll(debtors.stream().map(p -> new PersonEntity(p.getName())).collect(Collectors.toList()));
		nobt.addExpense(expense);

		em.merge(nobt);
		em.getTransaction().commit();

		return nobtMapper.map(expense);
	}

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
