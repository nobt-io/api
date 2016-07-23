package io.nobt.persistence.dao;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import io.nobt.core.UnknownNobtException;
import io.nobt.core.domain.Amount;
import io.nobt.core.domain.Expense;
import io.nobt.core.domain.Nobt;
import io.nobt.core.domain.NobtId;
import io.nobt.core.domain.Person;
import io.nobt.persistence.NobtDao;

/**
 * @author Matthias
 */
public class InMemoryNobtDao implements NobtDao {

    private static final AtomicLong idGenerator = new AtomicLong(1);
    private static final Map<NobtId, Nobt> nobtDatabase = new HashMap<>();

    @Override
    public Nobt create(String nobtName, Set<Person> explicitParticipants) {

        Nobt nobt = new Nobt(new NobtId(idGenerator.getAndIncrement()), nobtName, explicitParticipants);
        nobtDatabase.put(nobt.getId(), nobt);
        return nobt;
    }

    @Override
    public Expense createExpense(NobtId nobtId, String name, BigDecimal amount, Person debtee, Set<Person> debtors) {

        Nobt nobt = get(nobtId);

        Expense expense = new Expense(name, Amount.fromBigDecimal(amount), debtee);
        expense.setDebtors(debtors);

        nobt.addExpense(expense);
        return expense;
    }

    @Override
    public Nobt get(NobtId id) {
        return find(id).orElseThrow(() -> new UnknownNobtException(id));
    }

    @Override
    public Optional<Nobt> find(NobtId id) {
        return Optional.ofNullable(nobtDatabase.get(id));
    }
}
