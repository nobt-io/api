package io.nobt.core.domain;

import io.nobt.core.domain.transaction.Debt;
import io.nobt.test.domain.factories.ExpenseBuilder;
import io.nobt.test.domain.factories.NobtBuilder;
import org.junit.Test;

import java.util.List;

import static io.nobt.test.domain.factories.ShareFactory.randomShare;
import static io.nobt.test.domain.factories.StaticPersonFactory.thomas;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class UsecaseTests {

    @Test
    public void bug_singleBillWithSingleParticipantMustNotYieldTransaction() throws Exception {

        final Nobt nobt = new NobtBuilder()
                .withExpenses(
                        new ExpenseBuilder()
                                .withShares(randomShare(thomas))
                                .withDebtee(thomas)
                                .build()
                )
                .build();

        final List<Debt> optimizedDebts = nobt.getOptimizedDebts();
        assertThat(optimizedDebts, is(empty()));
    }

}
