package io.nobt.core.domain;

import org.junit.Test;

import java.util.Set;
import java.util.UUID;

import static io.nobt.core.PersonFactory.*;
import static io.nobt.util.Sets.newHashSet;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;

public class NobtTest {

	@Test
	public void testShouldReturnAllParticipatingPersonsOfAllExpenses() throws Exception {

		final Nobt billa = new Nobt("Billa", UUID.randomUUID());

		thomas = Person.forName("Thomas");
		final Expense obst = new Expense("Obst", Amount.fromDouble(10), thomas);
		thomas = Person.forName("Thomas");
		obst.setDebtors(newHashSet(thomas, lukas, matthias));

		final Expense bier = new Expense("Bier", Amount.fromDouble(30), david);
		thomas = Person.forName("Thomas");
		bier.setDebtors(newHashSet(thomas, thomasB, matthias, david));

		billa.addExpense(obst);
		billa.addExpense(bier);

		final Set<Person> actualPersons = billa.getParticipatingPersons();

		thomas = Person.forName("Thomas");
		assertThat(actualPersons, containsInAnyOrder(thomas, lukas, matthias, thomasB, david));
	}
}