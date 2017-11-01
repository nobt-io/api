package io.nobt.core;

import io.nobt.core.domain.Amount;
import io.nobt.core.domain.debt.Debt;
import io.nobt.core.domain.debt.combination.Add;
import io.nobt.core.domain.debt.combination.CompositeResult;
import io.nobt.core.domain.debt.combination.NotCombinable;
import io.nobt.core.domain.debt.combination.Remove;

import static io.nobt.core.domain.debt.Debt.debt;
import static io.nobt.test.domain.factories.StaticPersonFactory.*;
import static junitparams.JUnitParamsRunner.$;

public final class TransactionTestCases {

    private TransactionTestCases() {
    }

    public static Object[] provideUnnecessaryTransactionExamples() {
        return $(
                $(
                        debt(jacqueline, euro(10), jacqueline),
                        debt(matthias, euro(5), lukas),
                        new Remove(debt(jacqueline, euro(10), jacqueline))
                ),
                $(
                        debt(matthias, euro(5), lukas),
                        debt(jacqueline, euro(10), jacqueline),
                        new Remove(debt(jacqueline, euro(10), jacqueline))
                )
        );
    }

    public static Object[] provideSelfCombiningExample() {

        final Debt transaction = debt(jacqueline, euro(10), jacqueline);

        return $(
                $(
                        transaction,
                        transaction,
                        new Remove(transaction)
                ),
                $(
                        debt(matthias, euro(5), lukas),
                        debt(jacqueline, euro(10), jacqueline),
                        new Remove(
                                debt(jacqueline, euro(10), jacqueline)
                        )
                )
        );
    }

    public static Object[] provideCompensatingTransactionExamples() {
        return $(
                $(
                        debt(matthias, euro(10), thomas),
                        debt(thomas, euro(10), matthias),
                        new Remove(
                                debt(thomas, euro(10), matthias),
                                debt(matthias, euro(10), thomas)
                        )
                )
        );
    }

    public static Object[] provideNoActionTransactionExamples() {
        return $(
                $(
                        debt(matthias, euro(10), thomas),
                        debt(harald, euro(10), simon),
                        new NotCombinable()
                )
        );
    }

    public static Object[] provideMergingTransactionExamples() {
        return $(
                $(
                        debt(matthias, euro(10), thomas),
                        debt(matthias, euro(11), thomas),
                        new CompositeResult(
                                new Add(
                                        debt(matthias, euro(21), thomas)
                                ),
                                new Remove(
                                        debt(matthias, euro(10), thomas),
                                        debt(matthias, euro(11), thomas)
                                )
                        )
                )
        );
    }

    public static Object[] provideTriangulationTransactionExamples() {
        return $(
                $(
                        debt(matthias, euro(10), thomas),
                        debt(thomas, euro(10), lukas),
                        new CompositeResult(
                                new Remove(
                                        debt(matthias, euro(10), thomas),
                                        debt(thomas, euro(10), lukas)
                                ),
                                new Add(
                                        debt(matthias, euro(10), lukas)
                                )
                        )
                ),
                $(
                        debt(matthias, euro(10), thomas),
                        debt(thomas, euro(6), jacqueline),
                        new CompositeResult(
                                new Add(
                                        debt(matthias, euro(6), jacqueline),
                                        debt(matthias, euro(4), thomas)
                                ),
                                new Remove(
                                        debt(matthias, euro(10), thomas),
                                        debt(thomas, euro(6), jacqueline)
                                )
                        )
                ),
                $(
                        debt(matthias, euro(10), thomas),
                        debt(thomas, euro(11), david),
                        new CompositeResult(
                                new Remove(
                                        debt(matthias, euro(10), thomas),
                                        debt(thomas, euro(11), david)
                                ),
                                new Add(
                                        debt(matthias, euro(10), david),
                                        debt(thomas, euro(1), david)
                                )
                        )
                ),
                $(
                        debt(matthias, euro(10), thomas),
                        debt(lukas, euro(10), matthias),
                        new CompositeResult(
                                new Remove(
                                        debt(matthias, euro(10), thomas),
                                        debt(lukas, euro(10), matthias)
                                ),
                                new Add(
                                        debt(lukas, euro(10), thomas)
                                )
                        )
                ),
                $(
                        debt(matthias, euro(10), thomas),
                        debt(lukas, euro(6), matthias),
                        new CompositeResult(
                                new Remove(
                                        debt(matthias, euro(10), thomas),
                                        debt(lukas, euro(6), matthias)
                                ),
                                new Add(
                                        debt(matthias, euro(4), thomas),
                                        debt(lukas, euro(6), thomas)
                                )
                        )
                ),
                $(
                        debt(matthias, euro(10), thomas),
                        debt(lukas, euro(11), matthias),
                        new CompositeResult(
                                new Remove(
                                        debt(matthias, euro(10), thomas),
                                        debt(lukas, euro(11), matthias)

                                ),
                                new Add(
                                        debt(lukas, euro(1), matthias),
                                        debt(lukas, euro(10), thomas))
                        )
                )
        );
    }

    private static Amount euro(int amount) {
        return Amount.fromDouble(amount);
    }

}
