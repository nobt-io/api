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
import java.util.Set;
import java.util.stream.Collectors;

import static org.quicktheories.QuickTheory.qt;
import static org.quicktheories.generators.SourceDSL.bigDecimals;
import static org.quicktheories.generators.SourceDSL.lists;

public class OptimizerConsistencyTest {

    @Test
    public void MINIMAL_OPTIMIZER_V2_balancesShouldRemainTheSameBeforeAndAfterOptimization() {
        myQt(Optimizer.MINIMAL_AMOUNT_V2).check(Balances::areEqual);
    }

    @Test
    public void MINIMAL_AMOUNTS_AND_MINIMAL_NUMBER_OF_DEBTS_balancesShouldRemainTheSameBeforeAndAfterOptimization() {
        myQt(Optimizer.MINIMAL_AMOUNTS_AND_MINIMAL_NUMBER_OF_DEBTS).check(Balances::areEqual);
    }

    @Test
    public void MINIMAL_AMOUNT_V1_balancesShouldRemainTheSameBeforeAndAfterOptimization() {
        myQt(Optimizer.MINIMAL_AMOUNT_V1).check(Balances::areEqual);
    }

    private static class Balances {
        private final Map<Person, Amount> initialBalances;
        private final Map<Person, Amount> optimizedBalances;
        private final List<Debt> initialDebts;
        private final List<Debt> optimizedDebts;

        private Balances(List<Debt> initialDebts, Optimizer optimizer) {
            this.initialDebts = initialDebts;
            this.initialBalances = balances(this.initialDebts);
            this.optimizedDebts = optimizer.apply(initialDebts);
            this.optimizedBalances = balances(optimizedDebts);
        }

        private boolean areEqual() {
            Map<Person, Amount> initialBalances = this.initialBalances;
            Map<Person, Amount> optimizedBalances = this.optimizedBalances;

            Set<Map.Entry<Person, Amount>> filteredInitialBalances = initialBalances.entrySet().stream().filter(entry -> entry.getValue().isPositive()).collect(Collectors.toSet());
            Set<Map.Entry<Person, Amount>> filteredOptimizedBalances = optimizedBalances.entrySet().stream().filter(entry -> entry.getValue().isPositive()).collect(Collectors.toSet());

            return filteredInitialBalances.equals(filteredOptimizedBalances);
        }

        @Override
        public String toString() {
            return String.format("Balances{\n" +
                    "initialBalances=%s\n" +
                    "initialDebts=%s\n" +
                    "optimizedBalances=%s\n" +
                    "optimizedDebts=%s\n" +
                    "}", initialBalances, initialDebts, optimizedBalances, optimizedDebts);
        }
    }

    private TheoryBuilder<Balances> myQt(Optimizer optimizer) {
        return qt()
                .withGenerateAttempts(10000)
                .forAll(lists()
                        .of(debts())
                        .ofSizeBetween(10, 100)
                        .map(debts -> new Balances(debts, optimizer))
                );
    }

    private static Map<Person, Amount> balances(List<Debt> debts) {
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
        return bigDecimals()
                .ofBytes(4)
                .withScale(2)
                .assuming(amount -> amount.signum() != 0)
                .map(amount -> amount.abs())
                .map(Amount::fromBigDecimal);
    }
}
