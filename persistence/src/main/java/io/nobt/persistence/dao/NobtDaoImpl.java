package io.nobt.persistence.dao;

import io.nobt.core.UnknownNobtException;
import io.nobt.core.domain.*;
import io.nobt.persistence.NobtDao;
import io.nobt.persistence.entity.ExpenseEntity;
import io.nobt.persistence.entity.NobtEntity;
import io.nobt.persistence.mapping.ExpenseMapper;
import io.nobt.persistence.mapping.NobtMapper;
import io.nobt.persistence.mapping.ShareMapper;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

public class NobtDaoImpl implements NobtDao {

    private final EntityManager em;
    private final NobtMapper nobtMapper;
    private final ExpenseMapper expenseMapper;
    private final ShareMapper shareMapper;

    public NobtDaoImpl(EntityManager em, NobtMapper nobtMapper, ExpenseMapper expenseMapper, ShareMapper shareMapper) {
        this.em = em;
        this.nobtMapper = nobtMapper;
        this.expenseMapper = expenseMapper;
        this.shareMapper = shareMapper;
    }

    @Override
    public Nobt createNobt(String nobtName, Set<Person> explicitParticipants) {

        em.getTransaction().begin();

        final Set<String> participantsAsStringList = explicitParticipants.stream().map(Person::getName).collect(toSet());

        NobtEntity nobt = new NobtEntity(nobtName, participantsAsStringList);

        em.persist(nobt);
        em.getTransaction().commit();

        return nobtMapper.mapToDomain(nobt);
    }

    @Override
    public Expense createExpense(NobtId nobtId, String name, String splitStrategy, Person debtee, List<Share> shares) {

        em.getTransaction().begin();

        final ExpenseEntity expense = new ExpenseEntity();

        expense.setName(name);
        expense.setDebtee(debtee.getName());
        expense.setSplitStrategy(splitStrategy);
        expense.setShares(shareMapper.mapToByteArray(shares));


        final NobtEntity nobt = findNobtEntity(nobtId).orElseThrow(() -> new UnknownNobtException(nobtId));
        nobt.addExpense(expense);

        em.merge(nobt);
        em.getTransaction().commit();

        return expenseMapper.mapToDomain(expense);
    }

    private Optional<NobtEntity> findNobtEntity(NobtId nobtId) {
        return Optional.ofNullable(em.find(NobtEntity.class, nobtId.getId()));
    }

    @Override
    public Expense createExpense(NobtId nobtId, String name, BigDecimal amount, Person debtee, Set<Person> debtors) {

        if (debtors.isEmpty()) {
            throw new IllegalArgumentException("Cannot save expense with no debtors!");
        }

        final BigDecimal amountPerDebtor = amount.divide(BigDecimal.valueOf(debtors.size()));

        final List<Share> shares = debtors.stream().map(d -> new Share(d, Amount.fromBigDecimal(amountPerDebtor))).collect(toList());

        return createExpense(nobtId, name, "AUTO - EQUAL", debtee, shares);
    }

    @Override
    public Nobt get(NobtId id) {
        return find(id).orElseThrow(() -> new UnknownNobtException(id));
    }

    @Override
    public Optional<Nobt> find(NobtId nobtId) {

        final Optional<NobtEntity> nobt = findNobtEntity(nobtId);

        return nobt.map(nobtMapper::mapToDomain);
    }

}
