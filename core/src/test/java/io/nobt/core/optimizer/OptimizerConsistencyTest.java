package io.nobt.core.optimizer;

import io.nobt.core.domain.Amount;
import io.nobt.core.domain.Person;
import io.nobt.core.domain.debt.Debt;
import io.nobt.test.domain.factories.StaticPersonFactory;
import org.junit.Test;
import org.quicktheories.core.Gen;
import org.quicktheories.dsl.TheoryBuilder;
import org.quicktheories.generators.Generate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import static org.quicktheories.QuickTheory.qt;
import static org.quicktheories.generators.SourceDSL.doubles;
import static org.quicktheories.generators.SourceDSL.lists;

public class OptimizerConsistencyTest {

    @Test
    public void MINIMAL_OPTIMIZER_V2_balancesShouldRemainTheSameBeforeAndAfterOptimization() {
        myQt().check(balancesAreEqual(Optimizer.MINIMAL_AMOUNT_V2));
    }

    @Test
    public void MINIMAL_AMOUNTS_AND_MINIMAL_NUMBER_OF_DEBTS_balancesShouldRemainTheSameBeforeAndAfterOptimization() {
        myQt().check(balancesAreEqual(Optimizer.MINIMAL_AMOUNTS_AND_MINIMAL_NUMBER_OF_DEBTS));
    }

    private TheoryBuilder<List<Debt>> myQt() {
        return qt()
                .withGenerateAttempts(10000)
                .forAll(lists()
                        .of(debts())
                        .ofSizeBetween(10, 100));
    }

    private Predicate<List<Debt>> balancesAreEqual(Optimizer optimizer) {
        return debts -> {

            Map<Person, Amount> balancesBefore = balances(debts);

            List<Debt> optimizedDebts = optimizer.apply(debts);

            Map<Person, Amount> balancesAfter = balances(optimizedDebts);

            return balancesBefore.equals(balancesAfter);
        };
    }

    private Map<Person, Amount> balances(List<Debt> debts) {
        HashMap<Person, Amount> balances = new HashMap<>();

        for (Debt debt : debts) {
            balances.compute(debt.getDebtee(), (debtee, amount) -> {
                if (amount == null) {
                    return debt.getAmount();
                } else {
                    return amount.plus(debt.getAmount());
                }
            });
            balances.compute(debt.getDebtor(), (debtor, amount) -> {
                if (amount == null) {
                    return debt.getAmount().multiplyBy(BigDecimal.valueOf(-1));
                } else {
                    return amount.minus(debt.getAmount());
                }
            });
        }

        return balances;
    }

    private Gen<Person> persons() {
        return Generate.pick(StaticPersonFactory.ALL);
    }

    private Gen<Debt> debts() {
        return persons()
                .zip(amounts(), persons(), Debt::debt)
                .assuming(debt -> {
                    Person debtee = debt.getDebtee();
                    Person debtor = debt.getDebtor();

                    return !debtee.equals(debtor);
                });
    }

    private Gen<Amount> amounts() {
        return doubles()
                .from(0.01)
                .upToAndIncluding(10000)
                .map(Amount::fromDouble);
    }
}
