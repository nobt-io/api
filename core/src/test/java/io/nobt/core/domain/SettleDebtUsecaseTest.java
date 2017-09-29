package io.nobt.core.domain;

import io.nobt.core.optimizer.Optimizer;
import io.nobt.test.domain.factories.ShareFactory;
import io.nobt.util.Sets;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

import static io.nobt.test.domain.factories.AmountFactory.amount;
import static io.nobt.test.domain.factories.StaticPersonFactory.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class SettleDebtUsecaseTest {

    private Nobt sut;

    @Before
    public void setUp() throws Exception {
        final NobtFactory nobtFactory = new NobtFactory(() -> Optimizer.MINIMAL_AMOUNT_V2, () -> ZonedDateTime.now(ZoneOffset.UTC));
        sut = nobtFactory.create("Settle Debt Usecase", Sets.newHashSet(thomas, david, matthias), new CurrencyKey("EUR"));
    }

    @Test
    public void givenASingleBill_paymentShould() throws Exception {

        sut.addExpense("First bill", "EVENLY", thomas, Sets.newHashSet(
                ShareFactory.share(david, 5),
                ShareFactory.share(matthias, 5)
        ), LocalDate.now(), null);

        sut.addPayment(david, amount(5), thomas, "Settle debts!");

        final List<Debt> optimizedDebts = sut.getOptimizedDebts();

        assertThat(optimizedDebts, allOf(
                iterableWithSize(1),
                hasItem(Debt.debt(matthias, amount(5), thomas))
        ));
    }
}
