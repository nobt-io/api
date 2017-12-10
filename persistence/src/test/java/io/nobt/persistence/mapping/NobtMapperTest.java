package io.nobt.persistence.mapping;

import io.nobt.core.domain.Nobt;
import io.nobt.core.domain.NobtId;
import io.nobt.persistence.cashflow.expense.ExpenseEntity;
import io.nobt.persistence.cashflow.expense.ExpenseMapper;
import io.nobt.persistence.cashflow.payment.PaymentMapper;
import io.nobt.persistence.nobt.NobtEntity;
import io.nobt.persistence.nobt.NobtMapper;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

import static io.nobt.test.domain.provider.NobtBuilderProvider.aNobt;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class NobtMapperTest {

    private NobtMapper sut;

    @Mock
    private ExpenseMapper expenseMapperMock;

    @Mock
    private NobtDatabaseIdResolver nobtDatabaseIdResolverMock;

    @Before
    public void setUp() throws Exception {
        sut = new NobtMapper(nobtDatabaseIdResolverMock, expenseMapperMock, new PaymentMapper());
    }

    @Test
    public void shouldMapEveryExpenseUsingExpenseMapper() throws Exception {

        final ExpenseEntity firstExpense = new ExpenseEntity();
        final ExpenseEntity secondExpense = new ExpenseEntity();

        final NobtEntity entity = new NobtEntity();
        entity.setCurrency("EUR");

        entity.setId(123L);
        entity.addExpense(firstExpense);
        entity.addExpense(secondExpense);
        entity.setExternalId("abcd");


        sut.mapToDomainModel(entity);


        verify(expenseMapperMock).mapToDomainModel(firstExpense);
        verify(expenseMapperMock).mapToDomainModel(secondExpense);
    }

    @Test
    public void setsResolvedDatabaseId() throws Exception {

        when(nobtDatabaseIdResolverMock.resolveDatabaseId("abcd")).thenReturn(Optional.of(1L));

        final Nobt nobtToMap = aNobt().withId(new NobtId("abcd")).build();


        final NobtEntity mappedNobtEntity = sut.mapToDatabaseModel(nobtToMap);


        assertThat(mappedNobtEntity.getId(), Matchers.is(1L));
    }
}