package io.nobt.core;

import io.nobt.core.domain.debt.Debt;
import io.nobt.core.domain.debt.combination.Add;
import io.nobt.core.domain.debt.combination.CompositeResult;
import io.nobt.core.domain.debt.combination.NotCombinable;
import io.nobt.core.domain.debt.combination.Remove;

import static io.nobt.core.domain.debt.Debt.debt;
import static io.nobt.test.domain.factories.AmountFactory.amount;
import static io.nobt.test.domain.factories.StaticPersonFactory.*;
import static junitparams.JUnitParamsRunner.$;

public final class DebtTestCases {

    private DebtTestCases() {
    }

    public static Object[] provideUnnecessaryDebtExamples() {
        return $(
                $(
                        debt(jacqueline, amount(10), jacqueline),
                        debt(matthias, amount(5), lukas),
                        new Remove(debt(jacqueline, amount(10), jacqueline))
                ),
                $(
                        debt(matthias, amount(5), lukas),
                        debt(jacqueline, amount(10), jacqueline),
                        new Remove(debt(jacqueline, amount(10), jacqueline))
                )
        );
    }

    public static Object[] provideSelfCombiningExample() {

        final Debt debt = debt(jacqueline, amount(10), jacqueline);

        return $(
                $(
                        debt,
                        debt,
                        new Remove(debt)
                ),
                $(
                        debt(matthias, amount(5), lukas),
                        debt(jacqueline, amount(10), jacqueline),
                        new Remove(
                                debt(jacqueline, amount(10), jacqueline)
                        )
                )
        );
    }

    public static Object[] provideCompensatingDebtExamples() {
        return $(
                $(
                        debt(matthias, amount(10), thomas),
                        debt(thomas, amount(10), matthias),
                        new Remove(
                                debt(thomas, amount(10), matthias),
                                debt(matthias, amount(10), thomas)
                        )
                ),
                $(
                        debt(matthias, amount(10), thomas),
                        debt(thomas, amount(20), matthias),
                        new CompositeResult(
                                new Remove(
                                        debt(matthias, amount(10), thomas),
                                        debt(thomas, amount(20), matthias)
                                ),
                                new Add(
                                        debt(thomas, amount(10), matthias)
                                )
                        )
                )
        );
    }

    public static Object[] provideNoActionDebtExamples() {
        return $(
                $(
                        debt(matthias, amount(10), thomas),
                        debt(harald, amount(10), simon),
                        new NotCombinable()
                )
        );
    }

    public static Object[] provideMergingDebtExamples() {
        return $(
                $(
                        debt(matthias, amount(10), thomas),
                        debt(matthias, amount(11), thomas),
                        new CompositeResult(
                                new Add(
                                        debt(matthias, amount(21), thomas)
                                ),
                                new Remove(
                                        debt(matthias, amount(10), thomas),
                                        debt(matthias, amount(11), thomas)
                                )
                        )
                )
        );
    }

    public static Object[] provideTriangulationDebtExamples() {
        return $(
                $(
                        debt(matthias, amount(10), thomas),
                        debt(thomas, amount(10), lukas),
                        new CompositeResult(
                                new Remove(
                                        debt(matthias, amount(10), thomas),
                                        debt(thomas, amount(10), lukas)
                                ),
                                new Add(
                                        debt(matthias, amount(10), lukas)
                                )
                        )
                ),
                $(
                        debt(matthias, amount(10), thomas),
                        debt(thomas, amount(6), jacqueline),
                        new CompositeResult(
                                new Add(
                                        debt(matthias, amount(6), jacqueline),
                                        debt(matthias, amount(4), thomas)
                                ),
                                new Remove(
                                        debt(matthias, amount(10), thomas),
                                        debt(thomas, amount(6), jacqueline)
                                )
                        )
                ),
                $(
                        debt(matthias, amount(10), thomas),
                        debt(thomas, amount(11), david),
                        new CompositeResult(
                                new Remove(
                                        debt(matthias, amount(10), thomas),
                                        debt(thomas, amount(11), david)
                                ),
                                new Add(
                                        debt(matthias, amount(10), david),
                                        debt(thomas, amount(1), david)
                                )
                        )
                ),
                $(
                        debt(matthias, amount(10), thomas),
                        debt(lukas, amount(10), matthias),
                        new CompositeResult(
                                new Remove(
                                        debt(matthias, amount(10), thomas),
                                        debt(lukas, amount(10), matthias)
                                ),
                                new Add(
                                        debt(lukas, amount(10), thomas)
                                )
                        )
                ),
                $(
                        debt(matthias, amount(10), thomas),
                        debt(lukas, amount(6), matthias),
                        new CompositeResult(
                                new Remove(
                                        debt(matthias, amount(10), thomas),
                                        debt(lukas, amount(6), matthias)
                                ),
                                new Add(
                                        debt(matthias, amount(4), thomas),
                                        debt(lukas, amount(6), thomas)
                                )
                        )
                ),
                $(
                        debt(matthias, amount(10), thomas),
                        debt(lukas, amount(11), matthias),
                        new CompositeResult(
                                new Remove(
                                        debt(matthias, amount(10), thomas),
                                        debt(lukas, amount(11), matthias)

                                ),
                                new Add(
                                        debt(lukas, amount(1), matthias),
                                        debt(lukas, amount(10), thomas))
                        )
                )
        );
    }
}
