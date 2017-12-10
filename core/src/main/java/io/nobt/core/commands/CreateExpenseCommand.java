package io.nobt.core.commands;

import io.nobt.core.domain.ExpenseDraft;
import io.nobt.core.domain.Nobt;
import io.nobt.core.domain.NobtId;

public final class CreateExpenseCommand extends AbstractNobtModifyingCommand<Void> {

    private final ExpenseDraft expenseDraft;

    public CreateExpenseCommand(NobtId id, ExpenseDraft expenseDraft) {
        super(id);
        this.expenseDraft = expenseDraft;
    }

    @Override
    protected void modify(Nobt nobt) {
        nobt.createExpenseFrom(expenseDraft);
    }
}
