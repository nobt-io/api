package io.nobt.core;

import io.nobt.core.domain.Transaction;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static io.nobt.core.domain.Transaction.transaction;
import static java.util.Collections.emptySet;
import static junitparams.JUnitParamsRunner.$;

public final class TransactionTestCases {

	private TransactionTestCases() {
	}

	public static Object[] provideCompensatingTransactionExamples() {
		return $(
				$(
						transaction("Matthias", 10, "Thomas"),
						transaction("Thomas", 10, "Matthias"),
						noTransactions()
				)
		);
	}

	public static Object[] provideNoActionTransactionExamples() {
		return $(
				$(
						transaction("Matthias", 10, "Thomas"),
						transaction("Harald", 10, "Simon"),
						expected(
								transaction("Matthias", 10, "Thomas"),
								transaction("Harald", 10, "Simon")
						))
		);
	}

	public static Object[] provideMergingTransactionExamples() {
		return $(
				$(
						transaction("Matthias", 10, "Thomas"),
						transaction("Matthias", 11, "Thomas"),
						expected(
								transaction("Matthias", 21, "Thomas")
						)
				)
		);
	}

	public static Object[] provideTriangulationTransactionExamples() {
		return $(
				$(
						transaction("Matthias", 10, "Thomas"),
						transaction("Thomas", 10, "Lukas"),
						expected(
								transaction("Matthias", 10, "Lukas")
						)
				),
				$(
						transaction("Matthias", 10, "Thomas"),
						transaction("Thomas", 6, "David"),
						expected(
								transaction("Matthias", 6, "David"),
								transaction("Matthias", 4, "Thomas")
						)
				),
				$(
						transaction("Matthias", 10, "Thomas"),
						transaction("Thomas", 11, "David"),
						expected(
								transaction("Matthias", 10, "David"),
								transaction("Thomas", 1, "David")
						)
				),
				$(
						transaction("Matthias", 10, "Thomas"),
						transaction("Lukas", 10, "Matthias"),
						expected(
								transaction("Lukas", 10, "Thomas")
						)
				),
				$(
						transaction("Matthias", 10, "Thomas"),
						transaction("Lukas", 6, "Matthias"),
						expected(
								transaction("Matthias", 4, "Thomas"),
								transaction("Lukas", 6, "Thomas")
						)
				),
				$(
						transaction("Matthias", 10, "Thomas"),
						transaction("Lukas", 11, "Matthias"),
						expected(
								transaction("Lukas", 1, "Matthias"),
								transaction("Lukas", 10, "Thomas")

						)
				)
				);
	}

	private static Set<Object> noTransactions() {
		return emptySet();
	}

	private static Set<Transaction> expected(Transaction... transactions) {
		return new HashSet<>(Arrays.asList(transactions));
	}
}
