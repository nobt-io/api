package io.nobt.core.domain;

import io.nobt.core.domain.transaction.Debt;
import org.junit.Test;

import java.util.List;

import static io.nobt.core.optimizer.Optimizer.MINIMAL_AMOUNT_V2;
import static io.nobt.test.domain.factories.ExpenseBuilderProvider.anExpense;
import static io.nobt.test.domain.factories.NobtBuilderProvider.aNobt;
import static io.nobt.test.domain.factories.ShareFactory.randomShare;
import static io.nobt.test.domain.factories.StaticPersonFactory.thomas;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class UsecaseTests {

    @Test
    public void bug_singleBillWithSingleParticipantMustNotYieldTransaction() throws Exception {

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
