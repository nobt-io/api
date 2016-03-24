package io.nobt.core;

import static io.nobt.core.Transaction.transaction;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import io.nobt.core.domain.Expense;
import io.nobt.core.domain.Nobt;
import io.nobt.core.domain.Person;

public class NobtCalculatorTest {

	private NobtCalculator nobtCalculator;

	@Before
	public void setUp() {
		nobtCalculator = new NobtCalculator();
	}

	@Test
	public void testShouldSplitSingleExpenseEquallyAmongAllReceivers() {

		Nobt sampleNobt = new Nobt("Sample Nobt");
		Expense hofer = expense("Hofer", amount(20), "Hugo", "Thomas", "Matthias", "David", "Thomas B.");

		sampleNobt.addExpense(hofer);

		Set<Transaction> result = nobtCalculator.calculate(sampleNobt);

		Assert.assertThat(result.size(), Matchers.is(4));
		Assert.assertThat(result,
				Matchers.containsInAnyOrder(transaction("Thomas", 5, "Hugo"), transaction("Matthias", 5, "Hugo"),
						transaction("David", 5, "Hugo"), transaction("Thomas B.", 5, "Hugo")));
	}

	@Test
	public void testShouldSplit2ExpensesEquallyAmongAllReceivers() {

		Nobt sampleNobt = new Nobt("Sample Nobt");
		Expense hofer = expense("Hofer", amount(20), "Hugo", "Thomas", "Matthias", "David", "Thomas B.");
		Expense billa = expense("Billa", amount(40), "Matthias", "Thomas", "Hugo", "David", "Thomas B.");

		sampleNobt.addExpense(billa);
		sampleNobt.addExpense(hofer);

		Set<Transaction> result = nobtCalculator.calculate(sampleNobt);

		Assert.assertThat(result.size(), Matchers.is(5));
		Assert.assertThat(result,
				Matchers.anyOf(
						Matchers.containsInAnyOrder(transaction("Thomas", 15, "Matthias"),
								transaction("David", 5, "Hugo"), transaction("David", 10, "Matthias"),
								transaction("Thomas B.", 5, "Hugo"), transaction("Thomas B.", 10, "Matthias")),
						Matchers.containsInAnyOrder(transaction("Thomas", 10, "Matthias"),
								transaction("Thomas", 5, "Hugo"), transaction("David", 15, "Matthias"),
								transaction("Thomas B.", 5, "Hugo"), transaction("Thomas B.", 10, "Matthias")),
						Matchers.containsInAnyOrder(transaction("Thomas", 10, "Matthias"),
								transaction("Thomas", 5, "Hugo"), transaction("David", 10, "Matthias"),
								transaction("David", 5, "Hugo"), transaction("Thomas B.", 15, "Matthias"))));
	}

	private BigDecimal amount(double amount) {
		return new BigDecimal(amount);
	}

	private static Person person(String name) {
		return new Person(name);
	}

	private static Expense expense(String name, BigDecimal amount, String spender, String... receivers) {
		Set<Person> receiverPersons = Arrays.stream(receivers).map(Person::new).collect(Collectors.toSet());

		Expense expense = new Expense(name, amount, person(spender));
		expense.setDebtors(receiverPersons);

		return expense;
	}
}
