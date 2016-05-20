package io.nobt.persistence.dao;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.junit.Test;

import io.nobt.core.domain.Amount;
import io.nobt.core.domain.Expense;
import io.nobt.core.domain.Nobt;
import io.nobt.core.domain.Person;
import io.nobt.persistence.NobtDao;
import io.nobt.persistence.entity.ExpenseEntity;
import io.nobt.persistence.entity.NobtEntity;
import io.nobt.persistence.entity.PersonEntity;
import io.nobt.util.Sets;

public class NobtDaoTest extends AbstractDaoTest {

	@Test
	public void testFindNobt() {

		UUID uuid = UUID.randomUUID();
		String name = "TestName";
		insertNobt(uuid, name);

		entityManager.getTransaction().begin();
		entityManager.flush();

		NobtDao dao = new NobtDaoImpl(entityManager, new NobtMapper());
		Nobt nobt = dao.find(uuid);

		assertNotNull(nobt);
		assertEquals(name, nobt.getName());
		assertEquals(uuid, nobt.getId());

		assertEquals(1, nobt.getExpenses().size());
		assertExpense(nobt.getExpenses().iterator().next(), "expenseName", BigDecimal.TEN, "debtee");

		entityManager.getTransaction().commit();
	}

	@Test
	public void testCreateNobt() {

		String name = "testName";

		NobtDao dao = new NobtDaoImpl(entityManager, new NobtMapper());
		Nobt nobt = dao.create(name);

		dao.find(nobt.getId());

		assertNotNull(nobt);
		assertEquals(name, nobt.getName());
	}

	@Test
	public void testCreateExpense() {

		UUID uuid = UUID.randomUUID();
		insertNobt(uuid, "nobtName");

		flush();

		NobtDao dao = new NobtDaoImpl(entityManager, new NobtMapper());
		String expenseName = "expenseName";
		BigDecimal amount = BigDecimal.ONE;
		String debteeName = "debtee";
		dao.createExpense(uuid, expenseName, amount, Person.forName(debteeName),
				Sets.newHashSet(Person.forName("debtor1"), Person.forName("debtor2"), Person.forName("debtor3")));

		flush();

		Nobt nobt = dao.find(uuid);
		assertNotNull(nobt);
		assertEquals(2, nobt.getExpenses().size());
		assertExpense(nobt.getExpenses().iterator().next(), expenseName, amount, debteeName,
				Sets.newHashSet("debtor1", "debtor2", "debtor3"));
	}

	private void flush() {
		entityManager.getTransaction().begin();
		entityManager.flush();
		entityManager.getTransaction().commit();
	}

	private void assertExpense(Expense expense, String name, BigDecimal amount, String debteeName,
			Set<String> debtors) {
		assertExpense(expense, name, amount, debteeName);

		assertEquals(debtors.size(), expense.getDebtors().size());
		assertTrue(
				expense.getDebtors().stream().map(d -> d.getName()).collect(Collectors.toList()).containsAll(debtors));
	}

	private void assertExpense(Expense expense, String name, BigDecimal amount, String debteeName) {

		assertNotNull(expense);
		assertEquals(name, expense.getName());
		assertEquals(Amount.fromBigDecimal(amount), expense.getOverallAmount());
		assertEquals(debteeName, expense.getDebtee().getName());

	}

	private void insertNobt(UUID uuid, String name) {
		NobtEntity nobt = new NobtEntity(name, uuid);

		nobt.addExpense(new ExpenseEntity("expenseName", BigDecimal.TEN, new PersonEntity("debtee")));

		entityManager.getTransaction().begin();
		entityManager.persist(nobt);
		entityManager.getTransaction().commit();

	}

}