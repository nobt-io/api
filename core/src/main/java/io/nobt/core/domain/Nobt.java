package io.nobt.core.domain;

import io.nobt.core.ConversionInformationInconsistentException;
import io.nobt.core.optimizer.Optimizer;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;
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

    public Nobt(NobtId id, CurrencyKey currencyKey, String name, Set<Person> explicitParticipants, Set<Expense> expenses, ZonedDateTime createdOn, Optimizer optimizer) {
        this(id, currencyKey, name, explicitParticipants, expenses, Collections.emptySet(), createdOn, optimizer);
    }

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

    public List<Debt> getOptimizedDebts() {
        return optimizer.apply(getAllDebts());
    }

    private List<Debt> getAllDebts() {
        return Stream
                .of(expenses, payments)
                .flatMap(Collection::stream)
                .sorted(comparing(CashFlow::getCreatedOn))
                .map(CashFlow::calculateAccruingDebts)
                .sequential()
                .flatMap(Collection::stream)
                .collect(toList());
    }

    public ZonedDateTime getCreatedOn() {
        return createdOn;
    }

    public void addPayment(Person sender, Amount amount, Person recipient, String description) {

        if (!getParticipatingPersons().contains(sender)) {
            throw new PersonNotParticipatingException(sender);
        }

        if (!getParticipatingPersons().contains(recipient)) {
            throw new PersonNotParticipatingException(recipient);
        }

        final Payment payment = new Payment(sender, recipient, amount, description, ZonedDateTime.now(ZoneOffset.UTC));

        this.payments.add(payment);

    }

    public void addExpense(String name, String splitStrategy, Person debtee, Set<Share> shares, LocalDate date, ConversionInformation conversionInformation) {

        if (conversionInformation == null) {
            conversionInformation = ConversionInformation.sameCurrencyAs(this);
        }

        final boolean isSameCurrency = conversionInformation.getForeignCurrencyKey().equals(currencyKey);

        if (isSameCurrency && !conversionInformation.hasDefaultRate()) {
            throw new ConversionInformationInconsistentException(this, conversionInformation);
        }

        final Expense newExpense = new Expense(null, name, splitStrategy, debtee, conversionInformation, shares, date, ZonedDateTime.now(ZoneOffset.UTC));

        expenses.add(newExpense);
    }

    public void removeExpense(Long expenseId) {
        this.expenses.removeIf(e -> e.getId().equals(expenseId));
    }
}