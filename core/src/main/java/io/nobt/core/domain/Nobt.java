package io.nobt.core.domain;

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

    public Nobt(NobtId id, String name, Set<Person> explicitParticipants, Set<Expense> expenses) {
        this.id = id;
        this.name = name;
        this.explicitParticipants = explicitParticipants;
        this.expenses = new HashSet<>(expenses);
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

    public void addExpense(String name, String splitStrategy, Person debtee, Set<Share> shares) {

        final Expense newExpense = new Expense(name, splitStrategy, debtee, shares);

        expenses.add(newExpense);
    }
}