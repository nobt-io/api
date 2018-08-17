package io.nobt.core.domain;

import io.nobt.core.ConversionInformationInconsistentException;
import io.nobt.core.domain.debt.Debt;
import io.nobt.test.domain.matchers.PaymentMatchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InOrder;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

import static io.nobt.test.domain.factories.AmountFactory.amount;
import static io.nobt.test.domain.factories.StaticPersonFactory.david;
import static io.nobt.test.domain.factories.StaticPersonFactory.thomas;
import static io.nobt.test.domain.matchers.ExpenseMatchers.hasId;
import static io.nobt.test.domain.matchers.NobtMatchers.*;
import static io.nobt.test.domain.matchers.PaymentMatchers.*;
import static io.nobt.test.domain.provider.ExpenseBuilderProvider.anExpense;
import static io.nobt.test.domain.provider.ExpenseDraftBuilderProvider.anExpenseDraft;
import static io.nobt.test.domain.provider.NobtBuilderProvider.aNobt;
import static io.nobt.test.domain.provider.PaymentDraftBuilderProvider.aPaymentDraft;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class NobtTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldBuildListOfPersonsFromEveryExpense() throws Exception {

        final Expense firstExpense = mock(Expense.class);
        final Expense secondExpense = mock(Expense.class);

        final Nobt nobt = aNobt()
                .withExpenses(firstExpense, secondExpense)
                .build();


        nobt.getParticipatingPersons();


        verify(firstExpense).getParticipants();
        verify(secondExpense).getParticipants();
    }

    @Test
    public void shouldCalculateDebtsOfCashFlowSortedByCreatedOn() throws Exception {

        final ZonedDateTime now = ZonedDateTime.now();

        final Expense firstExpense = mock(Expense.class);
        final Expense secondExpense = mock(Expense.class);
        final Payment firstPayment = mock(Payment.class);

        when(firstExpense.getCreatedOn()).thenReturn(now.minusDays(3).toInstant());
        when(firstPayment.getCreatedOn()).thenReturn(now.minusDays(2).toInstant());
        when(secondExpense.getCreatedOn()).thenReturn(now.minusDays(1).toInstant());

        final Nobt nobt = aNobt()
                .withExpenses(firstExpense, secondExpense)
                .withPayments(firstPayment)
                .build();


        final List<Debt> optimizedDebts = nobt.getOptimizedDebts();


        final InOrder inOrder = inOrder(firstExpense, secondExpense, firstPayment);

        inOrder.verify(firstExpense).calculateAccruingDebts();
        inOrder.verify(firstPayment).calculateAccruingDebts();
        inOrder.verify(secondExpense).calculateAccruingDebts();
    }

    @Test
    public void shouldThrowExceptionIfConversionInformationIsNotConsistent() throws Exception {

        final Nobt nobt = aNobt()
                .withCurrency(new CurrencyKey("EUR"))
                .build();

        final ExpenseDraft expenseDraft = anExpenseDraft()
                .withConversionInformation(new ConversionInformation(new CurrencyKey("EUR"), BigDecimal.TEN))
                .build();


        expectedException.expect(ConversionInformationInconsistentException.class);
        nobt.createExpenseFrom(expenseDraft);
    }

    @Test
    public void shouldAddPaymentToTheListOfPayments() throws Exception {

        final Nobt nobt = aNobt().build();

        final PaymentDraft paymentDraft = aPaymentDraft()
                .withSender(thomas)
                .withRecipient(david)
                .withAmount(amount(5))
                .build();

        nobt.createPaymentFrom(paymentDraft);


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

        final Nobt nobt = aNobt().build();
        final ExpenseDraft expenseDraft = anExpenseDraft().build();


        nobt.createExpenseFrom(expenseDraft);


        final Expense expense = nobt.getExpenses().stream().findAny().orElseThrow(IllegalStateException::new);

        assertThat(expense.getConversionInformation().getForeignCurrencyKey(), is(new CurrencyKey("EUR")));
        assertThat(expense.getConversionInformation().getRate(), is(BigDecimal.ONE));
    }

    @Test
    public void shouldAddExpenseIfConversionInformationIsConsistent() throws Exception {

        final Nobt nobt = aNobt()
                .withCurrency(new CurrencyKey("EUR"))
                .build();

        final ExpenseDraft expenseDraft = anExpenseDraft()
                .withConversionInformation(new ConversionInformation(new CurrencyKey("USD"), BigDecimal.TEN))
                .build();


        nobt.createExpenseFrom(expenseDraft);
    }

    @Test
    public void shouldRemoveExpenseById() throws Exception {

        final Nobt nobt = aNobt()
                .withExpenses(anExpense().withId(1L))
                .build();


        nobt.deleteExpense(1L);


        assertThat(nobt, allOf(
                hasExpenses(iterableWithSize(0)),
                hasDeletedExpenses(iterableWithSize(1))
        ));
    }

    @Test
    public void givenDeletedExpense_whenNewExpenseIsAdded_mustNotReuseId() {

        final Nobt nobt = aNobt().withExpenses(anExpense().withId(1L)).build();

        nobt.deleteExpense(1L);


        nobt.createExpenseFrom(anExpenseDraft().build());


        assertThat(nobt, hasExpenses(contains(
                not(hasId(equalTo(1L)))
        )));
    }

    @Test
    public void shouldAssignIdToExpense() throws Exception {

        final Nobt nobt = aNobt().build();


        nobt.createExpenseFrom(anExpenseDraft().build());


        assertThat(nobt, hasExpenses(
                allOf(
                        iterableWithSize(greaterThan(0)),
                        everyItem(
                                hasId(notNullValue(Long.class))
                        )
                )
        ));
    }

    @Test
    public void shouldAssignIdToPayment() throws Exception {

        final Nobt nobt = aNobt().build();


        nobt.createPaymentFrom(aPaymentDraft().build());


        assertThat(nobt, hasPayments(
                allOf(
                        iterableWithSize(greaterThan(0)),
                        everyItem(
                                PaymentMatchers.hasId(notNullValue(Long.class))
                        )
                )
        ));
    }

    @Test
    public void shouldAssignOneAsTheFirstId() throws Exception {

        final Nobt nobt = aNobt().build();


        nobt.createExpenseFrom(anExpenseDraft().build());


        assertThat(nobt, hasExpenses(hasItem(hasId(equalTo(1L)))));
    }

    @Test
    public void shouldNotAssignSameIdsToExpensesAndPayments() throws Exception {

        final Nobt nobt = aNobt().build();


        nobt.createExpenseFrom(anExpenseDraft().build());
        nobt.createPaymentFrom(aPaymentDraft().build());


        final Payment payment = nobt.getPayments().iterator().next();
        final Expense expense = nobt.getExpenses().iterator().next();

        assertThat(payment, PaymentMatchers.hasId(not(equalTo(expense.getId()))));
    }
}