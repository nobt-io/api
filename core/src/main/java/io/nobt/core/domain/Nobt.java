package io.nobt.core.domain;

import io.nobt.core.ConversionInformationInconsistentException;
import io.nobt.core.optimizer.OptimizerStrategy;
import io.nobt.core.optimizer.OptimizerVersion;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

public class Nobt {

    private final NobtId id;
    private final CurrencyKey currencyKey;
    private final String name;
    private final Set<Person> explicitParticipants;
    private final Set<Expense> expenses;
    private final ZonedDateTime createdOn;
    private final OptimizerVersion optimizerVersion;

    public Nobt(NobtId id, CurrencyKey currencyKey, String name, Set<Person> explicitParticipants, Set<Expense> expenses, ZonedDateTime createdOn, OptimizerVersion optimizerVersion) {
        this.id = id;
        this.currencyKey = currencyKey;
        this.name = name;
        this.explicitParticipants = new HashSet<>(explicitParticipants);
        this.expenses = new HashSet<>(expenses);
        this.createdOn = createdOn;
	    this.optimizerVersion = optimizerVersion;
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

	public OptimizerVersion getOptimizerVersion() {
		return optimizerVersion;
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

    public List<Transaction> getOptimalTransactions() {

        final OptimizerStrategy optimizerStrategy = optimizerVersion.getStrategy();

        return optimizerStrategy.optimize(getAllTransactions());
    }

	private List<Transaction> getAllTransactions() {
		return expenses
				.stream()
				.flatMap(expense -> expense.getTransactions().stream())
				.collect(toList());
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
        this.expenses.removeIf( e -> e.getId().equals(expenseId) );
    }
}