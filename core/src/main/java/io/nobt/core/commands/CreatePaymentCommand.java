package io.nobt.core.commands;

import io.nobt.core.domain.Nobt;
import io.nobt.core.domain.NobtId;
import io.nobt.core.domain.PaymentDraft;

public final class CreatePaymentCommand extends AbstractNobtModifyingCommand<Void> {

    private final PaymentDraft paymentDraft;

    public CreatePaymentCommand(NobtId id, PaymentDraft paymentDraft) {
        super(id);
        this.paymentDraft = paymentDraft;
    }

    @Override
    protected void modify(Nobt nobt) {
        nobt.createPaymentFrom(paymentDraft);
    }
}
