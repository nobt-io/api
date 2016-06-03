package io.nobt.core;

import io.nobt.core.domain.Amount;
import io.nobt.core.domain.Transaction;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static io.nobt.core.PersonFactory.*;
import static io.nobt.core.domain.Transaction.transaction;
import static java.util.Collections.emptySet;
import static junitparams.JUnitParamsRunner.$;

public final class TransactionTestCases {

    private TransactionTestCases() {
    }

    public static Object[] provideUnnecessaryTransactionExamples() {
        return $(
                $(
                        transaction(jacqueline, euro(10), jacqueline),
                        transaction(matthias, euro(5), lukas),
                        expected(
                                transaction(matthias, euro(5), lukas)
                        )
                ),
                $(
                        transaction(matthias, euro(5), lukas),
                        transaction(jacqueline, euro(10), jacqueline),
                        expected(
                                transaction(matthias, euro(5), lukas)
                        )
                )
        );
    }

    public static Object[] provideCompensatingTransactionExamples() {
        return $(
                $(
                        transaction(matthias, euro(10), thomas),
                        transaction(thomas, euro(10), matthias),
                        noTransactions()
                )
        );
    }

    public static Object[] provideNoActionTransactionExamples() {
        return $(
                $(
                        transaction(matthias, euro(10), thomas),
                        transaction(harald, euro(10), simon),
                        expected(
                                transaction(matthias, euro(10), thomas),
                                transaction(harald, euro(10), simon)
                        ))
        );
    }

    public static Object[] provideMergingTransactionExamples() {
        return $(
                $(
                        transaction(matthias, euro(10), thomas),
                        transaction(matthias, euro(11), thomas),
                        expected(
                                transaction(matthias, euro(21), thomas)
                        )
                )
        );
    }

    public static Object[] provideTriangulationTransactionExamples() {
        return $(
                $(
                        transaction(matthias, euro(10), thomas),
                        transaction(thomas, euro(10), lukas),
                        expected(
                                transaction(matthias, euro(10), lukas)
                        )
                ),
                $(
                        transaction(matthias, euro(10), thomas),
                        transaction(thomas, euro(6), jacqueline),
                        expected(
                                transaction(matthias, euro(6), jacqueline),
                                transaction(matthias, euro(4), thomas)
                        )
                ),
                $(
                        transaction(matthias, euro(10), thomas),
                        transaction(thomas, euro(11), david),
                        expected(
                                transaction(matthias, euro(10), david),
                                transaction(thomas, euro(1), david)
                        )
                ),
                $(
                        transaction(matthias, euro(10), thomas),
                        transaction(lukas, euro(10), matthias),
                        expected(
                                transaction(lukas, euro(10), thomas)
                        )
                ),
                $(
                        transaction(matthias, euro(10), thomas),
                        transaction(lukas, euro(6), matthias),
                        expected(
                                transaction(matthias, euro(4), thomas),
                                transaction(lukas, euro(6), thomas)
                        )
                ),
                $(
                        transaction(matthias, euro(10), thomas),
                        transaction(lukas, euro(11), matthias),
                        expected(
                                transaction(lukas, euro(1), matthias),
                                transaction(lukas, euro(10), thomas)

                        )
                )
        );
    }

    private static Amount euro(int amount) {
        return Amount.fromDouble(amount);
    }

    private static Set<Object> noTransactions() {
        return emptySet();
    }

    private static Set<Transaction> expected(Transaction... transactions) {
        return new HashSet<>(Arrays.asList(transactions));
    }
}
