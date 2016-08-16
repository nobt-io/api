package io.nobt.persistence.dao;

import io.nobt.core.UnknownNobtException;
import io.nobt.core.domain.*;
import io.nobt.persistence.NobtDao;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Matthias
 */
public class InMemoryNobtDao implements NobtDao {

    private static final AtomicLong idGenerator = new AtomicLong(1);
    private static final Map<NobtId, Nobt> nobtDatabase = new HashMap<>();

    @Override
    public Nobt createNobt(String nobtName, Set<Person> explicitParticipants) {

        Nobt nobt = new Nobt(new NobtId(idGenerator.getAndIncrement()), nobtName, explicitParticipants);
        nobtDatabase.put(nobt.getId(), nobt);
        return nobt;
    }

    @Override
    public Expense createExpense(NobtId nobtId, String name, String splitStrategy, Person debtee, List<Share> shares) {
        final Nobt nobt = get(nobtId);

        final Expense expense = new Expense(name, splitStrategy, debtee);
        shares.forEach(expense::addShare);

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
