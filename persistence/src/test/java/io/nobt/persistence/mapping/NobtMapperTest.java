package io.nobt.persistence.mapping;

import io.nobt.persistence.expense.ExpenseEntity;
import io.nobt.persistence.expense.ExpenseMapper;
import io.nobt.persistence.nobt.NobtEntity;
import io.nobt.persistence.nobt.NobtMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class NobtMapperTest {

    private NobtMapper sut;

    @Mock
    private ExpenseMapper expenseMapper;

    @Before
    public void setUp() throws Exception {
        sut = new NobtMapper(expenseMapper);
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


        sut.mapToDomainModel(entity);


        verify(expenseMapper).mapToDomainModel(firstExpense);
        verify(expenseMapper).mapToDomainModel(secondExpense);
    }
}