package io.nobt.core.domain;

import io.nobt.core.ConversionInformationInconsistentException;
import io.nobt.core.optimizer.Optimizer;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

public class Nobt {

    private final NobtId id;
    private final CurrencyKey currencyKey;
    private final String name;
    private final Set<Person> explicitParticipants;
    private final Set<Expense> expenses;
    private final List<Payment> payments;
    private final ZonedDateTime createdOn;
    private final Optimizer optimizer;

    public Nobt(NobtId id, CurrencyKey currencyKey, String name, Set<Person> explicitParticipants, Set<Expense> expenses, ZonedDateTime createdOn, Optimizer optimizer) {
        this(id, currencyKey, name, explicitParticipants, expenses, Collections.emptyList(), createdOn, optimizer);
    }

    public Nobt(NobtId id, CurrencyKey currencyKey, String name, Set<Person> explicitParticipants, Set<Expense> expenses, List<Payment> payments, ZonedDateTime createdOn, Optimizer optimizer) {
        this.id = id;
        this.currencyKey = currencyKey;
        this.name = name;
        this.explicitParticipants = new HashSet<>(explicitParticipants);
        this.expenses = new HashSet<>(expenses);
        this.payments = new ArrayList<>(payments);
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

    public Set<Person> getParticipatingPersons() {

        final HashSet<Person> allPersons = new HashSet<>(explicitParticipants);

        expenses.stream()
                .flatMap(expense -> expense.getParticipants().stream())
                .forEach(allPersons::add);

        return allPersons;
    }

    public SuggestedTransactions getSuggestedTransactions() {

        final SuggestedTransactions suggestedTransactions = new SuggestedTransactions(optimizer);

        final List<Debt> debts = Stream
                .of(expenses, payments)
                .flatMap(Collection::stream)
                .sorted(comparing(CashFlow::getCreatedOn))
                .sequential()
                .flatMap(cashFlow -> cashFlow.calculateAccruingDebts().stream())
                .collect(toList());

        return suggestedTransactions;
    }


    public ZonedDateTime getCreatedOn() {
        return createdOn;
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