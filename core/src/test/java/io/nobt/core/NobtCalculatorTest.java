package io.nobt.core;

import io.nobt.core.domain.*;
import io.nobt.util.Sets;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;
import java.util.UUID;

import static io.nobt.core.PersonFactory.*;
import static io.nobt.core.domain.Transaction.transaction;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.containsInAnyOrder;

public class NobtCalculatorTest {

	private NobtCalculator nobtCalculator;

	@Before
	public void setUp() {
		nobtCalculator = new NobtCalculator();
	}

	@Test
	public void testShouldSplitSingleExpenseEquallyAmongAllReceivers() {

		Nobt sampleNobt = new Nobt(null, "Sample Nobt");
		Expense hofer = expense("Hofer", euro(20), lukas, thomas, matthias, david, thomasB);

		sampleNobt.addExpense(hofer);

		Set<Transaction> result = nobtCalculator.calculate(sampleNobt);

		Assert.assertThat(result.size(), Matchers.is(4));
		Assert.assertThat(result,
				containsInAnyOrder(
						transaction(thomas, euro(5), lukas),
						transaction(matthias, euro(5), lukas),
						transaction(david, euro(5), lukas),
						transaction(thomasB, euro(5), lukas)
				)
		);
	}

	@Test
	public void testShouldSplit2ExpensesEquallyAmongAllReceivers() {

		Nobt sampleNobt = new Nobt(null, "Sample Nobt");
		Expense hofer = expense("Hofer", euro(20), lukas, thomas, matthias, david, thomasB);
		Expense billa = expense("Billa", euro(40), matthias, thomas, lukas, david, thomasB);

		sampleNobt.addExpense(billa);
		sampleNobt.addExpense(hofer);

		Set<Transaction> result = nobtCalculator.calculate(sampleNobt);

		Assert.assertThat(result.size(), Matchers.is(5));
		Assert.assertThat(result,
				anyOf(
						containsInAnyOrder(
								transaction(thomas, euro(15), matthias),
								transaction(david, euro(5), lukas),
								transaction(david, euro(10), matthias),
								transaction(thomasB, euro(5), lukas),
								transaction(thomasB, euro(10), matthias)
						),
						containsInAnyOrder(
								transaction(thomas, euro(10), matthias),
								transaction(thomas, euro(5), lukas),
								transaction(david, euro(15), matthias),
								transaction(thomasB, euro(5), lukas),
								transaction(thomasB, euro(10), matthias)
						),
						containsInAnyOrder(
								transaction(thomas, euro(10), matthias),
								transaction(thomas, euro(5), lukas),
								transaction(david, euro(10), matthias),
								transaction(david, euro(5), lukas),
								transaction(thomasB, euro(15), matthias)
						)
				)
		);
	}

	private Amount euro(double amount) {
		return Amount.fromDouble(amount);
	}

	private static Expense expense(String name, Amount amount, Person spender, Person... debtors) {

		Expense expense = new Expense(name, amount, spender);
		expense.setDebtors(Sets.newHashSet(debtors));

		return expense;
	}
}
