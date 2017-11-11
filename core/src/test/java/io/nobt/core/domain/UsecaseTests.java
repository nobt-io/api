package io.nobt.core.domain;

import io.nobt.core.domain.debt.Debt;
import org.junit.Test;

import java.util.List;

import static io.nobt.core.optimizer.Optimizer.MINIMAL_AMOUNT_V2;
import static io.nobt.test.domain.factories.ShareFactory.randomShare;
import static io.nobt.test.domain.factories.StaticPersonFactory.thomas;
import static io.nobt.test.domain.provider.ExpenseBuilderProvider.anExpense;
import static io.nobt.test.domain.provider.NobtBuilderProvider.aNobt;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class UsecaseTests {

    @Test
    public void bug_singleBillWithSingleParticipantMustNotYieldDebt() throws Exception {

        final Nobt nobt = aNobt()
                .withOptimizer(MINIMAL_AMOUNT_V2)
                .withExpenses(
                        anExpense()
                                .withDebtee(thomas)
                                .withShares(randomShare(thomas))
                )
                .build();


        final List<Debt> optimizedDebts = nobt.getOptimizedDebts();


        assertThat(optimizedDebts, is(empty()));
    }

}
