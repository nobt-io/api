package io.nobt.core.domain;

import io.nobt.core.domain.debt.Debt;
import org.junit.Test;

import java.util.List;

import static io.nobt.core.domain.debt.Debt.debt;
import static io.nobt.core.optimizer.Optimizer.MINIMAL_AMOUNT_V2;
import static io.nobt.test.domain.factories.AmountFactory.amount;
import static io.nobt.test.domain.factories.ShareFactory.share;
import static io.nobt.test.domain.factories.StaticPersonFactory.*;
import static io.nobt.test.domain.provider.ExpenseBuilderProvider.anExpense;
import static io.nobt.test.domain.provider.NobtBuilderProvider.aNobt;
import static io.nobt.test.domain.provider.PaymentDraftBuilderProvider.aPaymentDraft;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class SettleDebtUsecaseTest {

    @Test
    public void givenASingleBill_paymentShouldSettleDebt() throws Exception {

        final Nobt nobt = aNobt()
                .withOptimizer(MINIMAL_AMOUNT_V2)
                .withExpenses(
                        anExpense()
                                .withDebtee(thomas)
                                .withShares(
                                        share(david, 5),
                                        share(matthias, 5)
                                )
                )
                .build();

        final PaymentDraft paymentDraft = aPaymentDraft()
                .withSender(david)
                .withRecipient(thomas)
                .withAmount(amount(5))
                .build();

        nobt.createPaymentFrom(paymentDraft);


        final List<Debt> optimizedDebts = nobt.getOptimizedDebts();


        assertThat(optimizedDebts, allOf(
                iterableWithSize(1),
                hasItem(debt(matthias, amount(5), thomas))
        ));
    }
}
