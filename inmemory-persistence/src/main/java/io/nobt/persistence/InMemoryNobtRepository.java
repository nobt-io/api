package io.nobt.persistence;

import io.nobt.core.UnknownNobtException;
import io.nobt.core.domain.Expense;
import io.nobt.core.domain.Nobt;
import io.nobt.core.domain.NobtId;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryNobtRepository implements NobtRepository {

    private static final AtomicLong idGenerator = new AtomicLong(1);
    private static final AtomicLong expenseIdGenerator = new AtomicLong(1);
    private static final Map<NobtId, Nobt> nobtDatabase = new HashMap<>();

    private static final Field nobtIdField;
    private static final Field expenseIdField;

    static {
        nobtIdField = findIdField(Nobt.class);
        nobtIdField.setAccessible(true);

        expenseIdField = findIdField(Expense.class);
        expenseIdField.setAccessible(true);
    }

    @Override
    public NobtId save(Nobt nobt) {

        final NobtId id = new NobtId(idGenerator.getAndIncrement());

        assignIds(nobt, id);

        nobtDatabase.put(id, nobt);

        return id;
    }

    @Override
    public Nobt getById(NobtId id) {
        return Optional.ofNullable(nobtDatabase.get(id)).orElseThrow(UnknownNobtException::new);
    }

    private void assignIds(Nobt nobt, NobtId id) {

        assignNobtId(nobt, id);
        assignExpenseIds(nobt);
    }

    private void assignNobtId(Nobt nobt, NobtId id) {
        try {
            nobtIdField.set(nobt, id);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    private void assignExpenseIds(Nobt nobt) {
        try {
            for (Expense expense : nobt.getExpenses()) {
                expenseIdField.set(expense, expenseIdGenerator.getAndIncrement());
            }
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    private static Field findIdField(Class<?> type) {
        try {
            return type.getDeclaredField("id");
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
