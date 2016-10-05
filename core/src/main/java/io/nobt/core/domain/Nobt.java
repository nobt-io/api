package io.nobt.core.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

public class Nobt {

    private final NobtId id;
    private final String name;
    private final Set<Person> explicitParticipants;
    private final Set<Expense> expenses;
    private final LocalDateTime createdOn;

    public Nobt(NobtId id, String name, Set<Person> explicitParticipants, Set<Expense> expenses, LocalDateTime createdOn) {
        this.id = id;
        this.name = name;
        this.explicitParticipants = explicitParticipants;
        this.expenses = new HashSet<>(expenses);
        this.createdOn = createdOn;
    }

    public NobtId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<Expense> getExpenses() {
        return Collections.unmodifiableSet(expenses);
    }

    public Set<Person> getParticipatingPersons() {

        final HashSet<Person> allPersons = new HashSet<>(explicitParticipants);

        expenses.stream()
                .flatMap(expense -> expense.getParticipants().stream() )
                .forEach(allPersons::add);

        return allPersons;
    }

    public List<Transaction> getAllTransactions() {
        return expenses
                .stream()
                .flatMap(expense -> expense.getTransactions().stream())
                .collect(toList());
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void addExpense(String name, String splitStrategy, Person debtee, Set<Share> shares, LocalDate date) {

        final Expense newExpense = new Expense(null, name, splitStrategy, debtee, shares, date, LocalDateTime.now(ZoneOffset.UTC));

        expenses.add(newExpense);
    }

    public void removeExpense(Long expenseId) {
        this.expenses.removeIf( e -> e.getId().equals(expenseId) );
    }
}