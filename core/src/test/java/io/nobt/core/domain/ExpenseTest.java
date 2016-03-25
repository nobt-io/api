package io.nobt.core.domain;

import org.junit.Test;

import java.util.Arrays;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Thomas Eizinger, Senacor Technologies AG.
 */
public class ExpenseTest {

	@Test
	public void testShouldCalculateAmountPerDebtorBasedOnDebtorSet() throws Exception {

		final Expense billa = new Expense("Billa", Amount.fromDouble(100), Person.personByName("Thomas"));

		final Set<Person> debtors = Arrays.asList("Lukas", "Matthias").stream().map(Person::personByName).collect(toSet());
		billa.setDebtors(debtors);

		assertThat(billa.getAmountPerDebtor(), is(Amount.fromDouble(50)));
	}
}