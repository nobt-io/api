package io.nobt.core.domain;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Nobt {

    private NobtId id;
    private String name;
    private final Set<Person> explicitParticipants;

    private Set<Expense> expenses = new HashSet<>();

    public Nobt(NobtId id, String name) {
        this(id, name, Collections.emptySet());
    }

    public Nobt(NobtId id, String name, Set<Person> explicitParticipants) {
        this.id = id;
        this.name = name;
        this.explicitParticipants = explicitParticipants;
    }

    public String getName() {
        return name;
    }

    public Set<Expense> getExpenses() {
        return expenses;
    }

    public Set<Person> getParticipatingPersons() {
        final Set<Person> participatingPersons = expenses
                .stream()
                .map(Expense::getDebtors)
                .collect(HashSet::new, HashSet::addAll, HashSet::addAll);

        expenses.stream().map(Expense::getDebtee).forEach(participatingPersons::add);
        explicitParticipants.stream().forEach(participatingPersons::add);

        return participatingPersons;
    }

    public void addExpense(Expense expense) {
        this.expenses.add(expense);
    }

    public NobtId getId() {
        return id;
    }
}
