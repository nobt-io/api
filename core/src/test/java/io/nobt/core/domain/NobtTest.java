package io.nobt.core.domain;

import io.nobt.core.ConversionInformationInconsistentException;
import io.nobt.util.Sets;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static io.nobt.test.domain.factories.AmountFactory.amount;
import static io.nobt.test.domain.factories.ShareFactory.randomShare;
import static io.nobt.test.domain.factories.StaticPersonFactory.*;
import static io.nobt.test.domain.matchers.NobtMatchers.hasExpenses;
import static io.nobt.test.domain.matchers.NobtMatchers.hasPayments;
import static io.nobt.test.domain.matchers.PaymentMatchers.*;
import static java.util.Collections.emptySet;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class NobtTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private Nobt sut;

    @Mock
    private Expense firstExpense;

    @Mock
    private Expense secondExpense;

    private NobtFactory nobtFactory = new NobtFactory();

    @Before
    public void setUp() throws Exception {

        sut = new Nobt(null, new CurrencyKey("EUR"), "Something", Sets.newHashSet(thomas), Sets.newHashSet(firstExpense, secondExpense), emptySet(), ZonedDateTime.now(ZoneOffset.UTC), null);

        when(firstExpense.getShares()).thenReturn(Sets.newHashSet(
                randomShare(david),
                randomShare(lukas)
        ));

        when(firstExpense.getShares()).thenReturn(Sets.newHashSet(
                randomShare(matthias),
                randomShare(simon)
        ));
    }

    @Test
    public void shouldBuildListOfPersonsFromEveryExpense() throws Exception {

        sut.getParticipatingPersons();

        verify(firstExpense).getParticipants();
        verify(secondExpense).getParticipants();
    }

    @Test
    public void shouldNotBeAbleToAddPaymentForNonParticipatingPerson() throws Exception {

        final Nobt sut = nobtFactory.create("Test", Sets.newHashSet(thomas, matthias, david), new CurrencyKey("EUR"));

        expectedException.expect(PersonNotParticipatingException.class);
        sut.addPayment(harald, amount(10), thomas, "Money money!");
    }

    @Test(expected = ConversionInformationInconsistentException.class)
    public void shouldThrowExceptionIfConversionInformationIsNotConsistent() throws Exception {

        final Nobt nobt = nobtFactory.create("Test", emptySet(), new CurrencyKey("EUR"));

        nobt.addExpense(null, null, david, emptySet(), null, new ConversionInformation(new CurrencyKey("EUR"), BigDecimal.TEN));
    }

    @Test
    public void shouldAddPaymentToTheListOfPayments() throws Exception {

        final Nobt nobt = nobtFactory.create("Test", Sets.newHashSet(thomas, david), new CurrencyKey("EUR"));

        nobt.addPayment(thomas, amount(5), david, "Money money!");

        assertThat(nobt, hasPayments(
                hasItem(
                        allOf(
                                hasSender(equalTo(thomas)),
                                hasRecipient(equalTo(david)),
                                hasAmount(equalTo(amount(5)))
                        )
                )
        ));
    }

    @Test
    public void shouldUseDefaultConversionInformationIfNonGiven() throws Exception {

        final Nobt nobt = nobtFactory.create("Test", emptySet(), new CurrencyKey("EUR"));

        nobt.addExpense("Test", "Some strategy", david, emptySet(), null, null);

        final Expense expense = nobt.getExpenses().stream().findAny().orElseThrow(IllegalStateException::new);

        assertThat(expense.getConversionInformation().getForeignCurrencyKey(), is(new CurrencyKey("EUR")));
        assertThat(expense.getConversionInformation().getRate(), is(BigDecimal.ONE));
    }

    @Test
    public void shouldAddExpenseIfConversionInformationIsConsistent() throws Exception {

        final Nobt nobt = nobtFactory.create("Test", emptySet(), new CurrencyKey("EUR"));

        nobt.addExpense(null, null, david, emptySet(), null, new ConversionInformation(new CurrencyKey("USD"), BigDecimal.TEN));
    }

    @Test
    public void shouldRemoveExpenseById() throws Exception {

        when(firstExpense.getId()).thenReturn(1L);
        when(secondExpense.getId()).thenReturn(2L);

        sut.removeExpense(1L);

        assertThat(sut, hasExpenses(iterableWithSize(1)));
    }
}