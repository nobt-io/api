package io.nobt.persistence.dao;

import io.nobt.core.domain.Amount;
import io.nobt.core.domain.Expense;
import io.nobt.core.domain.Nobt;
import io.nobt.core.domain.NobtId;
import io.nobt.core.domain.Person;
import io.nobt.persistence.NobtDao;
import io.nobt.persistence.entity.ExpenseEntity;
import io.nobt.persistence.entity.NobtEntity;
import io.nobt.persistence.mapping.NobtMapper;
import io.nobt.util.Sets;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import static io.nobt.matchers.NobtMatchers.*;
import static io.nobt.matchers.PersonMatchers.personWithName;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

// TODO rewrite
public class NobtDaoTest extends AbstractDaoTest {

	/*
	@Test
	public void testFindNobt() {

		String name = "TestName";
        final NobtId id = insertNobt(name);

		NobtDao dao = new NobtDaoImpl(entityManager, new NobtMapper(bsonMapper), expenseMapper, shareMapper);

		Nobt nobt = dao.get(id);

		assertEquals(name, nobt.getName());

		assertEquals(1, nobt.getExpenses().size());
		assertExpense(nobt.getExpenses().iterator().next(), "expenseName", BigDecimal.TEN, "debtee");
	}

	@Test
	public void testCreateNobt() {

		String name = "testName";

		NobtDao dao = new NobtDaoImpl(entityManager, new NobtMapper(bsonMapper), expenseMapper, shareMapper);
		Nobt nobt = dao.create(name, Sets.newHashSet( Person.forName("Thomas") ));

        final Nobt retrievedNobt = dao.get(nobt.getId());

		assertThat(retrievedNobt, allOf(
				hasName(is(name)),
				hasParticipants(hasItem(personWithName(is("Thomas"))))
		));
	}

	@Test
	public void testCreateExpense() {

        final NobtId id = insertNobt("nobtName");

        flush();

		NobtDao dao = new NobtDaoImpl(entityManager, new NobtMapper(bsonMapper), expenseMapper, shareMapper);

		String expenseName = "expenseName";
		BigDecimal amount = BigDecimal.ONE;
		String debteeName = "debtee";

        dao.createExpense(id, expenseName, amount, Person.forName(debteeName),
				Sets.newHashSet(Person.forName("debtor1"), Person.forName("debtor2"), Person.forName("debtor3")));

		flush();

		Nobt nobt = dao.get(id);

		assertEquals(2, nobt.getExpenses().size());
		assertExpense(nobt.getExpenses().iterator().next(), expenseName, amount, debteeName, Sets.newHashSet("debtor1", "debtor2", "debtor3"));
	}

	private void flush() {
		entityManager.getTransaction().begin();
		entityManager.flush();
		entityManager.getTransaction().commit();
	}

	private void assertExpense(Expense expense, String name, BigDecimal amount, String debteeName, Set<String> debtors) {
		assertExpense(expense, name, amount, debteeName);

		assertEquals(debtors.size(), expense.getParticipants().size());
		assertTrue(expense.getParticipants().stream().map(d -> d.getName()).collect(Collectors.toList()).containsAll(debtors));
	}

	private void assertExpense(Expense expense, String name, BigDecimal amount, String debteeName) {

		assertNotNull(expense);
		assertEquals(name, expense.getName());
		assertEquals(Amount.fromBigDecimal(amount), expense.getOverallAmount());
		assertEquals(debteeName, expense.getDebtee().getName());

	}

	private NobtId insertNobt(String name) {
		NobtEntity nobt = new NobtEntity(name, Collections.emptySet());

		nobt.addExpense(new ExpenseEntity("expenseName", BigDecimal.TEN, "debtee"));

		entityManager.getTransaction().begin();
		entityManager.persist(nobt);
		entityManager.getTransaction().commit();

        return new NobtId(nobt.getId());
	}
	*/
}
