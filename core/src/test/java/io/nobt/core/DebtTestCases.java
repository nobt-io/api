package io.nobt.core;

import io.nobt.core.domain.transaction.Debt;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static io.nobt.core.domain.transaction.Debt.debt;
import static io.nobt.test.domain.factories.AmountFactory.amount;
import static io.nobt.test.domain.factories.StaticPersonFactory.*;
import static java.util.Collections.emptySet;
import static junitparams.JUnitParamsRunner.$;

public final class DebtTestCases {

    private DebtTestCases() {
    }

    public static Object[] provideUnnecessaryTransactionExamples() {
        return $(
                $(
                        debt(jacqueline, amount(10), jacqueline),
                        debt(matthias, amount(5), lukas),
                        expected(
                                debt(matthias, amount(5), lukas)
                        )
                ),
                $(
                        debt(matthias, amount(5), lukas),
                        debt(jacqueline, amount(10), jacqueline),
                        expected(
                                debt(matthias, amount(5), lukas)
                        )
                )
        );
    }

    public static Object[] provideCompensatingTransactionExamples() {
        return $(
                $(
                        debt(matthias, amount(10), thomas),
                        debt(thomas, amount(10), matthias),
                        noDebts()
                )
        );
    }

    public static Object[] provideNoActionTransactionExamples() {
        return $(
                $(
                        debt(matthias, amount(10), thomas),
                        debt(harald, amount(10), simon),
                        expected(
                                debt(matthias, amount(10), thomas),
                                debt(harald, amount(10), simon)
                        ))
        );
    }

    public static Object[] provideMergingTransactionExamples() {
        return $(
                $(
                        debt(matthias, amount(10), thomas),
                        debt(matthias, amount(11), thomas),
                        expected(
                                debt(matthias, amount(21), thomas)
                        )
                )
        );
    }

    public static Object[] provideTriangulationTransactionExamples() {
        return $(
                $(
                        debt(matthias, amount(10), thomas),
                        debt(thomas, amount(10), lukas),
                        expected(
                                debt(matthias, amount(10), lukas)
                        )
                ),
                $(
                        debt(matthias, amount(10), thomas),
                        debt(thomas, amount(6), jacqueline),
                        expected(
                                debt(matthias, amount(6), jacqueline),
                                debt(matthias, amount(4), thomas)
                        )
                ),
                $(
                        debt(matthias, amount(10), thomas),
                        debt(thomas, amount(11), david),
                        expected(
                                debt(matthias, amount(10), david),
                                debt(thomas, amount(1), david)
                        )
                ),
                $(
                        debt(matthias, amount(10), thomas),
                        debt(lukas, amount(10), matthias),
                        expected(
                                debt(lukas, amount(10), thomas)
                        )
                ),
                $(
                        debt(matthias, amount(10), thomas),
                        debt(lukas, amount(6), matthias),
                        expected(
                                debt(matthias, amount(4), thomas),
                                debt(lukas, amount(6), thomas)
                        )
                ),
                $(
                        debt(matthias, amount(10), thomas),
                        debt(lukas, amount(11), matthias),
                        expected(
                                debt(lukas, amount(1), matthias),
                                debt(lukas, amount(10), thomas)

                        )
                )
        );
    }

    private static Set<Debt> noDebts() {
        return emptySet();
    }

    private static Set<Debt> expected(Debt... debts) {
        return new HashSet<>(Arrays.asList(debts));
    }
}
