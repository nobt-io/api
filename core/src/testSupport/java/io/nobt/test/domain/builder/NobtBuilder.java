package io.nobt.test.domain.builder;

import io.nobt.core.domain.*;
import io.nobt.core.optimizer.Optimizer;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public class NobtBuilder {

    private Set<Expense> expenses;
    private Set<DeletedExpense> deletedExpenses;
    private Set<Person> participants;
    private Set<Payment> payments;
    private ZonedDateTime dateTime;
    private CurrencyKey currencyKey;
    private String name;
    private Optimizer optimizer;
    private NobtId id;

    public NobtBuilder withExpenses(Set<Expense> expenses) {
        this.expenses = expenses;
        return this;
    }

    public NobtBuilder withExpenses(Expense... expenses) {
        return withExpenses(Arrays.stream(expenses).collect(toSet()));
    }

    public NobtBuilder withExpenses(ExpenseBuilder... expenseBuilders) {
        return withExpenses(Arrays.stream(expenseBuilders).map(ExpenseBuilder::build).collect(toSet()));
    }

    public NobtBuilder withDeletedExpenses(Set<Expense> expenses) {
        this.deletedExpenses = expenses.stream().map(DeletedExpense::new).collect(toSet());
        return this;
    }

    public NobtBuilder withDeletedExpenses(Expense... expenses) {
        return withDeletedExpenses(Arrays.stream(expenses).collect(toSet()));
    }

    public NobtBuilder withDeletedExpenses(ExpenseBuilder... expenseBuilders) {
        return withDeletedExpenses(Arrays.stream(expenseBuilders).map(ExpenseBuilder::build).collect(toSet()));
    }

    public NobtBuilder withPayments(PaymentBuilder... payments) {
        return withPayments(Arrays.stream(payments).map(PaymentBuilder::build).collect(toSet()));
    }

    public NobtBuilder withPayments(Payment... payments) {
        return withPayments(Arrays.stream(payments).collect(toSet()));
    }

    public NobtBuilder withPayments(Set<Payment> payments) {
        this.payments = payments;
        return this;
    }

    public NobtBuilder withParticipants(Person... persons) {
        return withParticipants(Arrays.stream(persons).collect(toSet()));
    }

    public NobtBuilder withParticipants(Set<Person> persons) {
        this.participants = persons;
        return this;
    }

    public NobtBuilder onDate(ZonedDateTime dateTime) {
        this.dateTime = dateTime;
        return this;
    }

    public NobtBuilder withCurrency(CurrencyKey currencyKey) {
        this.currencyKey = currencyKey;
        return this;
    }

    public NobtBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public NobtBuilder withOptimizer(Optimizer optimizer) {
        this.optimizer = optimizer;
        return this;
    }

    public NobtBuilder withId(NobtId id) {
        this.id = id;
        return this;
    }

    public Nobt build() {
        return new Nobt(
                id,
                currencyKey,
                name,
                participants,
                expenses,
                deletedExpenses,
                payments,
                dateTime,
                optimizer
        );
    }
}
