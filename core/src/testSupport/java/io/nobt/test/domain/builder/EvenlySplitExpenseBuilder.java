package io.nobt.test.domain.builder;

import io.nobt.core.domain.Amount;
import io.nobt.core.domain.Expense;
import io.nobt.core.domain.Person;
import io.nobt.core.domain.Share;
import io.nobt.util.Sets;

import java.math.BigDecimal;
import java.util.Set;

import static io.nobt.test.domain.factories.AmountFactory.amount;
import static io.nobt.test.domain.factories.ShareFactory.share;
import static java.util.stream.Collectors.toSet;

public class EvenlySplitExpenseBuilder {

    private final ExpenseBuilder expenseBuilder;

    public EvenlySplitExpenseBuilder(ExpenseBuilder expenseBuilder) {
        this.expenseBuilder = expenseBuilder;
    }

    public EvenlySplitExpenseBuilder() {
        expenseBuilder = new ExpenseBuilder();
    }

    private Set<Person> debtors;
    private Amount total;

    public EvenlySplitExpenseBuilder withDebtee(Person debtee) {
        expenseBuilder.withDebtee(debtee);
        return this;
    }

    public EvenlySplitExpenseBuilder withDebtors(Person... debtors) {
        this.debtors = Sets.newHashSet(debtors);
        return this;
    }

    public EvenlySplitExpenseBuilder withTotal(double amount) {
        this.total = amount(amount);
        return this;
    }

    public Expense build() {

        final Set<Share> shares = debtors.stream()
                .map(debtor -> share(debtor, total.divideBy(BigDecimal.valueOf(debtors.size()))))
                .collect(toSet());

        expenseBuilder
                .withShares(shares)
                .withSplitStrategy("EVENLY");

        return expenseBuilder.build();
    }
}
