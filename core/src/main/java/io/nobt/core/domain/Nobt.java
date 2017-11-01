package io.nobt.core.domain;

import io.nobt.core.domain.transaction.Debt;
import io.nobt.core.optimizer.Optimizer;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
    private final Set<Payment> payments;
    private final ZonedDateTime createdOn;
    private final Optimizer optimizer;

    public Nobt(NobtId id, CurrencyKey currencyKey, String name, Set<Person> explicitParticipants, Set<Expense> expenses, Set<Payment> payments, ZonedDateTime createdOn, Optimizer optimizer) {
        this.id = id;
        this.currencyKey = currencyKey;
        this.name = name;
        this.explicitParticipants = new HashSet<>(explicitParticipants);
        this.expenses = new HashSet<>(expenses);
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

    public Optimizer getOptimizer() {
        return optimizer;
    }

    public Set<Expense> getExpenses() {
        return Collections.unmodifiableSet(expenses);
    }

    public Set<Payment> getPayments() {
        return payments;
    }

    public Set<Person> getParticipatingPersons() {

        final Set<Person> participantsFromExpenses = expenses.stream().map(Expense::getParticipants).flatMap(Collection::stream).collect(toSet());

        return Stream.of(explicitParticipants, participantsFromExpenses)
                .flatMap(Collection::stream)
                .collect(toCollection(HashSet::new));
    }

    private Stream<CashFlow> getAllCashFlows() {
        return Stream.of(expenses, payments).flatMap(Collection::stream);
    }

    public List<Debt> getOptimizedDebts() {
        return optimizer.apply(getAllDebts());
    }

    private List<Debt> getAllDebts() {
        return getAllCashFlows()
                .sorted(comparing(CashFlow::getCreatedOn))
                .map(CashFlow::calculateAccruingDebts)
                .sequential()
                .flatMap(Collection::stream)
                .collect(toList());
    }

    public ZonedDateTime getCreatedOn() {
        return createdOn;
    }

    public void addPayment(Person sender, Amount amount, Person recipient, String description, LocalDate date) {

        if (!getParticipatingPersons().contains(sender)) {
            throw new PersonNotParticipatingException(sender);
        }

        if (!getParticipatingPersons().contains(recipient)) {
            throw new PersonNotParticipatingException(recipient);
        }

        final Payment payment = new Payment(getNextIdentifier(), sender, recipient, amount, description, date, ZonedDateTime.now(ZoneOffset.UTC));

        payments.add(payment);
    }

    private long getNextIdentifier() {
        final long highestIdentifier = getAllCashFlows()
                .map(CashFlow::getId)
                .max(comparingLong(l -> l))
                .orElse(1L);

        return highestIdentifier + 1;
    }

    public void removeExpense(long expenseId) {
        this.expenses.removeIf(e -> e.getId() == expenseId);
    }

    public void createExpenseFrom(ExpenseDraft expenseDraft) {

        final Expense expense = Expense.fromDraft(getNextIdentifier(), currencyKey, expenseDraft);

        expenses.add(expense);
    }
}