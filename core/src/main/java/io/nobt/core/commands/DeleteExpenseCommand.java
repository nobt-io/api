package io.nobt.core.commands;

import io.nobt.core.domain.Nobt;
import io.nobt.core.domain.NobtId;

public final class DeleteExpenseCommand extends AbstractNobtModifyingCommand<Void> {

    private final Long expenseId;

    public DeleteExpenseCommand(NobtId id, Long expenseId) {
        super(id);
        this.expenseId = expenseId;
    }

    @Override
    protected void modify(Nobt nobt) {
        nobt.removeExpense(expenseId);
    }
}
