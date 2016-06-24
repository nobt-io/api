package io.nobt.core.domain;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Nobt {

    private UUID id;
    private String name;
    private final Set<Person> explicitParticipants;

    private Set<Expense> expenses = new HashSet<>();

    public Nobt(String name, UUID id) {
        this(name, id, Collections.emptySet());
    }

    public Nobt(String name, UUID uuid, Set<Person> explicitParticipants) {
        this.name = name;
        this.id = uuid;
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

    public UUID getId() {
        return id;
    }

}
