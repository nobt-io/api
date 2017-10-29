package io.nobt.persistence.mapping;

import io.nobt.core.domain.CurrencyKey;
import io.nobt.core.domain.Nobt;
import io.nobt.core.domain.NobtId;
import io.nobt.core.optimizer.Optimizer;
import io.nobt.persistence.entity.ExpenseEntity;
import io.nobt.persistence.entity.NobtEntity;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
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
        sut = new NobtMapper(nobtDatabaseIdResolverMock, expenseMapperMock);
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

        when(nobtDatabaseIdResolverMock.resolveDatabaseId(any())).thenReturn(Optional.of(1L));

        final Nobt nobtToMap = new Nobt(new NobtId("abcd"), new CurrencyKey("EUR"), "Test", Collections.emptySet(), Collections.emptySet(), ZonedDateTime.now(), Optimizer.MINIMAL_AMOUNT_V1);


        final NobtEntity mappedNobtEntity = sut.mapToDatabaseModel(nobtToMap);


        assertThat(mappedNobtEntity.getId(), Matchers.is(1L));
    }
}