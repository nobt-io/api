package io.nobt.core;

import static io.nobt.core.domain.Transaction.transaction;

import java.util.Set;

import io.nobt.core.domain.Transaction;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

public class TransactionTest {

	@Test
	public void testTransactionsShouldCompensateEachOther() throws Exception {

		Transaction transaction1 = transaction("Matthias", 10, "Thomas");
		Transaction transaction2 = transaction("Thomas", 10, "Matthias");

		Set<Transaction> resultingTransactions = transaction1.combine(transaction2);

		Assert.assertTrue(resultingTransactions.isEmpty());
	}

	@Test
	public void testBothTransactionsShouldBeReturnedIfNoActionCanBeTaken() throws Exception {

		Transaction transaction1 = transaction("Matthias", 10, "Thomas");
		Transaction transaction2 = transaction("Harald", 10, "Simon");

		Set<Transaction> resultingTransactions = transaction1.combine(transaction2);

		Assert.assertThat(resultingTransactions.size(), Matchers.is(2));
		Assert.assertThat(resultingTransactions, Matchers.containsInAnyOrder(transaction1, transaction2));
	}

	@Test
	public void testTransactionsWith3ParticipantsShouldReduceToOneIfAmountsMatch() throws Exception {

		Transaction transaction1 = transaction("Matthias", 10, "Thomas");
		Transaction transaction2 = transaction("Thomas", 10, "Lukas");

		Set<Transaction> resultingTransactions = transaction1.combine(transaction2);

		Transaction expected = transaction("Matthias", 10, "Lukas");

		Assert.assertThat(resultingTransactions.size(), Matchers.is(1));
		Assert.assertThat(resultingTransactions, Matchers.containsInAnyOrder(expected));
	}

	@Test
	public void testTransactionsWith3ParticipantsShouldReduceToOneIfAmountsMatchReversed() throws Exception {

		Transaction transaction1 = transaction("Matthias", 10, "Thomas");
		Transaction transaction2 = transaction("Lukas", 10, "Matthias");

		Set<Transaction> resultingTransactions = transaction1.combine(transaction2);

		Transaction expected = transaction("Lukas", 10, "Thomas");

		Assert.assertThat(resultingTransactions.size(), Matchers.is(1));
		Assert.assertThat(resultingTransactions, Matchers.containsInAnyOrder(expected));
	}

	@Test
	public void testTransactionsWith3ParticipantsShouldResultInTwoIfAmountDifferenceIsPositive() throws Exception {

		Transaction transaction1 = transaction("Matthias", 10, "Thomas");
		Transaction transaction2 = transaction("Thomas", 6, "David");

		Set<Transaction> resultingTransactions = transaction1.combine(transaction2);

		Transaction expected1 = transaction("Matthias", 6, "David");
		Transaction expected2 = transaction("Matthias", 4, "Thomas");

		Assert.assertThat(resultingTransactions.size(), Matchers.is(2));
		Assert.assertThat(resultingTransactions, Matchers.containsInAnyOrder(expected1, expected2));
	}

	@Test
	public void testTransactionsWith3ParticipantsShouldResultInTwoIfAmountDifferenceIsNegative() throws Exception {

		Transaction transaction1 = transaction("Matthias", 10, "Thomas");
		Transaction transaction2 = transaction("Thomas", 11, "David");

		Set<Transaction> resultingTransactions = transaction1.combine(transaction2);

		Transaction expected1 = transaction("Matthias", 10, "David");
		Transaction expected2 = transaction("Thomas", 1, "David");

		Assert.assertThat(resultingTransactions.size(), Matchers.is(2));
		Assert.assertThat(resultingTransactions, Matchers.containsInAnyOrder(expected1, expected2));
	}

	@Test
	public void testTransactionsWith3ParticipantsShouldResultInTwoIfAmountDifferenceIsPositiveReversed()
			throws Exception {

		Transaction transaction1 = transaction("Matthias", 10, "Thomas");
		Transaction transaction2 = transaction("Lukas", 6, "Matthias");

		Set<Transaction> resultingTransactions = transaction1.combine(transaction2);

		Transaction expected1 = transaction("Matthias", 4, "Thomas");
		Transaction expected2 = transaction("Lukas", 6, "Thomas");

		Assert.assertThat(resultingTransactions.size(), Matchers.is(2));
		Assert.assertThat(resultingTransactions, Matchers.containsInAnyOrder(expected1, expected2));
	}

	@Test
	public void testTransactionsWith3ParticipantsShouldResultInTwoIfAmountDifferenceIsNegativeReversed()
			throws Exception {

		Transaction transaction1 = transaction("Matthias", 10, "Thomas");
		Transaction transaction2 = transaction("Lukas", 11, "Matthias");

		Set<Transaction> resultingTransactions = transaction1.combine(transaction2);

		Transaction expected1 = transaction("Lukas", 1, "Matthias");
		Transaction expected2 = transaction("Lukas", 10, "Thomas");

		Assert.assertThat(resultingTransactions.size(), Matchers.is(2));
		Assert.assertThat(resultingTransactions, Matchers.containsInAnyOrder(expected1, expected2));
	}

	@Test
	public void testTransactionsWith2ParticipantsShouldMergeIfSameDirection() throws Exception {

		Transaction transaction1 = transaction("Matthias", 10, "Thomas");
		Transaction transaction2 = transaction("Matthias", 11, "Thomas");

		Set<Transaction> resultingTransactions = transaction1.combine(transaction2);

		Transaction expected1 = transaction("Matthias", 21, "Thomas");

		Assert.assertThat(resultingTransactions.size(), Matchers.is(1));
		Assert.assertThat(resultingTransactions, Matchers.containsInAnyOrder(expected1));
	}
}
