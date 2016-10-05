package io.nobt.core.domain;

import io.nobt.core.ConversionInformationInconsistentException;
import io.nobt.test.domain.factories.StaticPersonFactory;
import io.nobt.test.domain.matchers.NobtMatchers;
import io.nobt.util.Sets;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;

import static io.nobt.test.domain.factories.ShareFactory.randomShare;
import static io.nobt.test.domain.factories.StaticPersonFactory.*;
import static io.nobt.test.domain.matchers.NobtMatchers.*;
import static java.util.Collections.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class NobtTest {

    private Nobt sut;

    @Mock
    private Expense firstExpense;

    @Mock
    private Expense secondExpense;

    private NobtFactory nobtFactory = new NobtFactory();

    @Before
    public void setUp() throws Exception {

        sut = new Nobt(null, new CurrencyKey("EUR"), "Something", Sets.newHashSet(thomas), Sets.newHashSet(firstExpense, secondExpense), LocalDateTime.now(ZoneOffset.UTC));

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
    public void shouldBuildListOfTransactionsFromEveryExpense() throws Exception {

        sut.getAllTransactions();

        verify(firstExpense).getTransactions();
        verify(secondExpense).getTransactions();
    }

    @Test
    public void shouldBuildListOfPersonsFromEveryExpense() throws Exception {

        sut.getParticipatingPersons();

        verify(firstExpense).getParticipants();
        verify(secondExpense).getParticipants();
    }

    @Test(expected = ConversionInformationInconsistentException.class)
    public void shouldThrowExceptionIfConversionInformationIsNotConsistent() throws Exception {

        final Nobt nobt = nobtFactory.create("Test", emptySet(), new CurrencyKey("EUR"));

        nobt.addExpense(null, null, david, emptySet(), null, new ConversionInformation(new CurrencyKey("EUR"), BigDecimal.TEN));
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
}