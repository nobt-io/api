package io.nobt.core;

import io.nobt.core.domain.Amount;
import io.nobt.core.domain.Debt;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static io.nobt.core.domain.Debt.debt;
import static io.nobt.test.domain.factories.StaticPersonFactory.*;
import static java.util.Collections.emptySet;
import static junitparams.JUnitParamsRunner.$;

public final class TransactionTestCases {

    private TransactionTestCases() {
    }

    public static Object[] provideUnnecessaryTransactionExamples() {
        return $(
                $(
                        debt(jacqueline, euro(10), jacqueline),
                        debt(matthias, euro(5), lukas),
                        expected(
                                debt(matthias, euro(5), lukas)
                        )
                ),
                $(
                        debt(matthias, euro(5), lukas),
                        debt(jacqueline, euro(10), jacqueline),
                        expected(
                                debt(matthias, euro(5), lukas)
                        )
                )
        );
    }

    public static Object[] provideCompensatingTransactionExamples() {
        return $(
                $(
                        debt(matthias, euro(10), thomas),
                        debt(thomas, euro(10), matthias),
                        noTransactions()
                )
        );
    }

    public static Object[] provideNoActionTransactionExamples() {
        return $(
                $(
                        debt(matthias, euro(10), thomas),
                        debt(harald, euro(10), simon),
                        expected(
                                debt(matthias, euro(10), thomas),
                                debt(harald, euro(10), simon)
                        ))
        );
    }

    public static Object[] provideMergingTransactionExamples() {
        return $(
                $(
                        debt(matthias, euro(10), thomas),
                        debt(matthias, euro(11), thomas),
                        expected(
                                debt(matthias, euro(21), thomas)
                        )
                )
        );
    }

    public static Object[] provideTriangulationTransactionExamples() {
        return $(
                $(
                        debt(matthias, euro(10), thomas),
                        debt(thomas, euro(10), lukas),
                        expected(
                                debt(matthias, euro(10), lukas)
                        )
                ),
                $(
                        debt(matthias, euro(10), thomas),
                        debt(thomas, euro(6), jacqueline),
                        expected(
                                debt(matthias, euro(6), jacqueline),
                                debt(matthias, euro(4), thomas)
                        )
                ),
                $(
                        debt(matthias, euro(10), thomas),
                        debt(thomas, euro(11), david),
                        expected(
                                debt(matthias, euro(10), david),
                                debt(thomas, euro(1), david)
                        )
                ),
                $(
                        debt(matthias, euro(10), thomas),
                        debt(lukas, euro(10), matthias),
                        expected(
                                debt(lukas, euro(10), thomas)
                        )
                ),
                $(
                        debt(matthias, euro(10), thomas),
                        debt(lukas, euro(6), matthias),
                        expected(
                                debt(matthias, euro(4), thomas),
                                debt(lukas, euro(6), thomas)
                        )
                ),
                $(
                        debt(matthias, euro(10), thomas),
                        debt(lukas, euro(11), matthias),
                        expected(
                                debt(lukas, euro(1), matthias),
                                debt(lukas, euro(10), thomas)

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

    private static Set<Debt> expected(Debt... transactions) {
        return new HashSet<>(Arrays.asList(transactions));
    }
}
