package io.nobt.core.domain;

import io.nobt.core.UnknownExpenseException;
import io.nobt.core.domain.debt.Debt;
import io.nobt.core.optimizer.Optimizer;

import java.time.Instant;
import java.util.*;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;
import static java.util.Comparator.comparingLong;
import static java.util.stream.Collectors.*;

public class Nobt {

    private final NobtId id;
    private final CurrencyKey currencyKey;
    private final String name;
    private final Set<Person> explicitParticipants;
    private final Set<Expense> expenses;
    private final Set<DeletedExpense> deletedExpenses;
    private final Set<Payment> payments;
    private final Instant createdOn;
    private final Optimizer optimizer;

    public Nobt(NobtId id, CurrencyKey currencyKey, String name, Set<Person> explicitParticipants, Set<Expense> expenses, Set<DeletedExpense> deletedExpenses, Set<Payment> payments, Instant createdOn, Optimizer optimizer) {
        this.id = id;
        this.currencyKey = currencyKey;
        this.name = name;
        this.explicitParticipants = new HashSet<>(explicitParticipants);
        this.expenses = new HashSet<>(expenses);
        this.deletedExpenses = new HashSet<>(deletedExpenses);
        this.payments = new HashSet<>(payments);
        this.createdOn = createdOn;
        this.optimizer = optimizer;
    }

    public NobtId getId() {
        return id;
    }

    public CurrencyKey getCurrencyKey() {
        return currencyKey;
    }

    public String getName() {
        return name;
    }

    private static long incrementByOne(long value) {
        return value + 1;
    }

    public Optimizer getOptimizer() {
        return optimizer;
    }

    public Set<Expense> getExpenses() {
        return Collections.unmodifiableSet(expenses);
    }

    public Set<DeletedExpense> getDeletedExpenses() {
        return Collections.unmodifiableSet(deletedExpenses);
    }

    public Set<Payment> getPayments() {
        return payments;
    }

    public Set<Person> getParticipatingPersons() {

        final Set<Person> participantsFromCashFlows = Stream.of(expenses, payments)
                .flatMap(Collection::stream)
                .map(CashFlow::getParticipants)
                .flatMap(Collection::stream)
                .collect(toSet());

        return Stream.of(explicitParticipants, participantsFromCashFlows)
                .flatMap(Collection::stream)
                .collect(toCollection(HashSet::new));
    }

    private Stream<CashFlow> getAllCashFlows() {
        return Stream.of(expenses, payments).flatMap(Collection::stream);
    }

    public List<Debt> getOptimizedDebts() {
        return optimizer.apply(getAllDebts());
    }

    public Instant getCreatedOn() {
        return createdOn;
    }

    private List<Debt> getAllDebts() {
        return getAllCashFlows()
                .sorted(comparing(CashFlow::getCreatedOn))
                .map(CashFlow::calculateAccruingDebts)
                .sequential()
                .flatMap(Collection::stream)
                .collect(toList());
    }

    public void createExpenseFrom(ExpenseDraft expenseDraft) {

        final Expense expense = Expense.fromDraft(getNextIdentifier(), currencyKey, expenseDraft);

        expenses.add(expense);
    }

    public void createPaymentFrom(PaymentDraft paymentDraft) {

        final Payment payment = Payment.fromDraft(getNextIdentifier(), currencyKey, paymentDraft);

        payments.add(payment);
    }

    private long getNextIdentifier() {

        final List<Long> deletedIds = deletedExpenses.stream().map(DeletedExpense::getId).collect(toList());
        final List<Long> otherIds = getAllCashFlows().map(CashFlow::getId).collect(toList());

        return Stream.of(deletedIds, otherIds)
                .flatMap(Collection::stream)
                .max(comparingLong(id -> id))
                .map(Nobt::incrementByOne)
                .orElse(1L);
    }

    public void deleteExpense(long expenseId) {

        Expense expenseToDelete = expenses.stream()
                .filter(e -> e.getId() == expenseId)
                .findFirst()
                .orElseThrow(UnknownExpenseException::new);

        expenses.remove(expenseToDelete);
        deletedExpenses.add(new DeletedExpense(expenseToDelete));
    }

}