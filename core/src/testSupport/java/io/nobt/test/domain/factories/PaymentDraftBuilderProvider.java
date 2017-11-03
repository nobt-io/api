package io.nobt.test.domain.factories;

import java.time.LocalDate;

import static io.nobt.test.domain.factories.AmountFactory.randomAmount;
import static io.nobt.test.domain.factories.RandomPersonFactory.randomPerson;

public final class PaymentDraftBuilderProvider {

    public static PaymentDraftBuilder aPaymentDraft() {
        return new PaymentDraftBuilder()
                .withDate(LocalDate.now().minusDays(1))
                .withSender(randomPerson())
                .withRecipient(randomPerson())
                .withAmount(randomAmount());
    }

}
