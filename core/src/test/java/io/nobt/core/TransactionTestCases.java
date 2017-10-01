package io.nobt.core;

import io.nobt.core.domain.Amount;
import io.nobt.core.domain.transaction.Transaction;
import io.nobt.core.domain.transaction.combination.Add;
import io.nobt.core.domain.transaction.combination.CombinationResult;
import io.nobt.core.domain.transaction.combination.CompositeResult;
import io.nobt.core.domain.transaction.combination.Remove;

import static io.nobt.core.domain.transaction.Transaction.transaction;
import static io.nobt.test.domain.factories.StaticPersonFactory.*;
import static junitparams.JUnitParamsRunner.$;

public final class TransactionTestCases {

    private TransactionTestCases() {
    }

    public static Object[] provideUnnecessaryTransactionExamples() {
        return $(
                $(
                        transaction(jacqueline, euro(10), jacqueline),
                        transaction(matthias, euro(5), lukas),
                        new Remove(transaction(jacqueline, euro(10), jacqueline))
                ),
                $(
                        transaction(matthias, euro(5), lukas),
                        transaction(jacqueline, euro(10), jacqueline),
                        new Remove(transaction(jacqueline, euro(10), jacqueline))
                )
        );
    }

    public static Object[] provideSelfCombiningExample() {

        final Transaction transaction = transaction(jacqueline, euro(10), jacqueline);

        return $(
                $(
                        transaction,
                        transaction,
                        new Remove(transaction)
                ),
                $(
                        transaction(matthias, euro(5), lukas),
                        transaction(jacqueline, euro(10), jacqueline),
                        new Remove(
                                transaction(jacqueline, euro(10), jacqueline)
                        )
                )
        );
    }

    public static Object[] provideCompensatingTransactionExamples() {
        return $(
                $(
                        transaction(matthias, euro(10), thomas),
                        transaction(thomas, euro(10), matthias),
                        new Remove(
                                transaction(thomas, euro(10), matthias),
                                transaction(matthias, euro(10), thomas)
                        )
                )
        );
    }

    public static Object[] provideNoActionTransactionExamples() {
        return $(
                $(
                        transaction(matthias, euro(10), thomas),
                        transaction(harald, euro(10), simon),
                        CombinationResult.NotCombinable
                )
        );
    }

    public static Object[] provideMergingTransactionExamples() {
        return $(
                $(
                        transaction(matthias, euro(10), thomas),
                        transaction(matthias, euro(11), thomas),
                        new CompositeResult(
                                new Add(
                                        transaction(matthias, euro(21), thomas)
                                ),
                                new Remove(
                                        transaction(matthias, euro(10), thomas),
                                        transaction(matthias, euro(11), thomas)
                                )
                        )
                )
        );
    }

    public static Object[] provideTriangulationTransactionExamples() {
        return $(
                $(
                        transaction(matthias, euro(10), thomas),
                        transaction(thomas, euro(10), lukas),
                        new CompositeResult(
                                new Remove(
                                        transaction(matthias, euro(10), thomas),
                                        transaction(thomas, euro(10), lukas)
                                ),
                                new Add(
                                        transaction(matthias, euro(10), lukas)
                                )
                        )
                ),
                $(
                        transaction(matthias, euro(10), thomas),
                        transaction(thomas, euro(6), jacqueline),
                        new CompositeResult(
                                new Add(
                                        transaction(matthias, euro(6), jacqueline),
                                        transaction(matthias, euro(4), thomas)
                                ),
                                new Remove(
                                        transaction(matthias, euro(10), thomas),
                                        transaction(thomas, euro(6), jacqueline)
                                )
                        )
                ),
                $(
                        transaction(matthias, euro(10), thomas),
                        transaction(thomas, euro(11), david),
                        new CompositeResult(
                                new Remove(
                                        transaction(matthias, euro(10), thomas),
                                        transaction(thomas, euro(11), david)
                                ),
                                new Add(
                                        transaction(matthias, euro(10), david),
                                        transaction(thomas, euro(1), david)
                                )
                        )
                ),
                $(
                        transaction(matthias, euro(10), thomas),
                        transaction(lukas, euro(10), matthias),
                        new CompositeResult(
                                new Remove(
                                        transaction(matthias, euro(10), thomas),
                                        transaction(lukas, euro(10), matthias)
                                ),
                                new Add(
                                        transaction(lukas, euro(10), thomas)
                                )
                        )
                ),
                $(
                        transaction(matthias, euro(10), thomas),
                        transaction(lukas, euro(6), matthias),
                        new CompositeResult(
                                new Remove(
                                        transaction(matthias, euro(10), thomas),
                                        transaction(lukas, euro(6), matthias)
                                ),
                                new Add(
                                        transaction(matthias, euro(4), thomas),
                                        transaction(lukas, euro(6), thomas)
                                )
                        )
                ),
                $(
                        transaction(matthias, euro(10), thomas),
                        transaction(lukas, euro(11), matthias),
                        new CompositeResult(
                                new Remove(
                                        transaction(matthias, euro(10), thomas),
                                        transaction(lukas, euro(11), matthias)

                                ),
                                new Add(
                                        transaction(lukas, euro(1), matthias),
                                        transaction(lukas, euro(10), thomas))
                        )
                )
        );
    }

    private static Amount euro(int amount) {
        return Amount.fromDouble(amount);
    }

}
