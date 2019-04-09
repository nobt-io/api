package io.nobt.core.optimizer;

import io.nobt.core.domain.Amount;
import io.nobt.core.domain.debt.Debt;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static io.nobt.core.domain.debt.Debt.debt;
import static io.nobt.test.domain.factories.AmountFactory.amount;
import static io.nobt.test.domain.factories.StaticPersonFactory.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class MinimalNumberOfDebtsOptimizerStrategyTest {

    private OptimizerStrategy sut;

    @Before
    public void setUp() throws Exception {
        sut = new MinimalNumberOfDebtsOptimizerStrategy();
    }

    @Test
    public void given_small_amount_should_avoid_by_moving_to_other_person() {

        final Debt simon_marlene = debt(simon, amount(11.30), marlene);
        final Debt thomas_marlene = debt(thomas, amount(3.81), marlene);
        final Debt simon_eva = debt(simon, amount(30.58), eva);
        final Debt thomas_eva = debt(thomas, amount(26.47), eva);

        final List<Debt> initialDebts = Arrays.asList(simon_marlene, thomas_marlene, simon_eva, thomas_eva);


        final List<Debt> optimizedDebts = sut.optimize(initialDebts);


        final Debt thomas_eva_new = thomas_eva.withNewAmount(thomas_eva.getAmount().plus(thomas_marlene.getAmount()));
        final Debt simon_marlene_new = simon_marlene.withNewAmount(simon_marlene.getAmount().plus(thomas_marlene.getAmount()));
        final Debt simon_eva_new = simon_eva.withNewAmount(simon_eva.getAmount().minus(thomas_marlene.getAmount()));

        final List<Debt> expectedOptimizedDebts = Arrays.asList(simon_marlene_new, thomas_eva_new, simon_eva_new);

        final BigDecimal initialSum = initialDebts.stream().reduce(BigDecimal.ZERO, (sum, next) -> next.getRoundedAmount().add(sum), BigDecimal::add);
        final BigDecimal optimizedSum = optimizedDebts.stream().reduce(BigDecimal.ZERO, (sum, next) -> next.getRoundedAmount().add(sum), BigDecimal::add);
        final BigDecimal expectedSum = expectedOptimizedDebts.stream().reduce(BigDecimal.ZERO, (sum, next) -> next.getRoundedAmount().add(sum), BigDecimal::add);

        // small consistency check before the actual test
        assertThat(initialSum, equalTo(optimizedSum));
        assertThat(expectedSum, equalTo(optimizedSum));

        assertThat(optimizedDebts, containsInAnyOrder(simon_marlene_new, thomas_eva_new, simon_eva_new));
    }

    @Test
    public void should_not_fail_if_no_optimization_is_possible() {

        // this cannot be optimized because there is no overlap between the debts
        final List<Debt> debts = Arrays.asList(
                debt(matthias, amount(2), thomas),
                debt(david, amount(12), thomasB),
                debt(jacqueline, amount(1), simon)
        );


        final List<Debt> optimizedDebts = sut.optimize(debts);


        assertThat(optimizedDebts, is(debts));
    }
}