package io.nobt.core.domain;

import io.nobt.core.ConversionInformationInconsistentException;
import io.nobt.test.domain.matchers.PaymentMatchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;

import static io.nobt.test.domain.factories.AmountFactory.amount;
import static io.nobt.test.domain.factories.ExpenseBuilderProvider.anExpense;
import static io.nobt.test.domain.factories.ExpenseDraftBuilderProvider.anExpenseDraft;
import static io.nobt.test.domain.factories.NobtBuilderProvider.aNobt;
import static io.nobt.test.domain.factories.PaymentDraftBuilderProvider.aPaymentDraft;
import static io.nobt.test.domain.factories.StaticPersonFactory.*;
import static io.nobt.test.domain.matchers.ExpenseMatchers.hasId;
import static io.nobt.test.domain.matchers.NobtMatchers.hasExpenses;
import static io.nobt.test.domain.matchers.NobtMatchers.hasPayments;
import static io.nobt.test.domain.matchers.PaymentMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
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
    public void shouldNotBeAbleToAddPaymentForNonParticipatingPerson() throws Exception {

        final Nobt sut = aNobt()
                .withParticipants(thomas, matthias, david)
                .build();


        final PaymentDraft paymentDraftWithUnknownPersons = aPaymentDraft()
                .withSender(harald)
                .withRecipient(thomasB)
                .build();

        expectedException.expect(PersonNotParticipatingException.class);
        sut.createPaymentFrom(paymentDraftWithUnknownPersons);
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

        final Nobt nobt = aNobt()
                .withParticipants(thomas, david)
                .build();

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


        nobt.removeExpense(1L);


        assertThat(nobt, hasExpenses(iterableWithSize(0)));
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

        final Nobt nobt = aNobt()
                .withParticipants(thomas, matthias)
                .build();


        nobt.createPaymentFrom(
                aPaymentDraft()
                        .withSender(thomas)
                        .withRecipient(matthias)
                        .build()
        );


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
    public void shouldNotAssignSameIdsToExpensesAndPayments() throws Exception {

        final Nobt nobt = aNobt()
                .withParticipants(thomas, matthias)
                .build();


        nobt.createExpenseFrom(anExpenseDraft().build());
        nobt.createPaymentFrom(aPaymentDraft().withSender(thomas).withRecipient(matthias).build());


        final Payment payment = nobt.getPayments().iterator().next();
        final Expense expense = nobt.getExpenses().iterator().next();

        assertThat(payment, PaymentMatchers.hasId(not(equalTo(expense.getId()))));
    }
}