package io.nobt.core.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

public class Nobt {

    private final NobtId id;
    private final String name;
    private final Set<Person> explicitParticipants;
    private final Set<Expense> expenses = new HashSet<>();

    public Nobt(NobtId id, String name, Set<Person> explicitParticipants) {
        this.id = id;
        this.name = name;
        this.explicitParticipants = explicitParticipants;
    }

    public NobtId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<Expense> getExpenses() {
        return expenses;
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

    public Nobt addExpense(Expense expense) {
        this.expenses.add(expense);
        return this;
    }
}
