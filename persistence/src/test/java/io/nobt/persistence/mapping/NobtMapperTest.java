package io.nobt.persistence.mapping;

import io.nobt.persistence.entity.ExpenseEntity;
import io.nobt.persistence.entity.NobtEntity;
import io.nobt.persistence.mapping.ExpenseMapper;
import io.nobt.persistence.mapping.NobtMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static java.util.Collections.emptySet;
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

        final NobtEntity entity = new NobtEntity(null, emptySet());
        entity.addExpense(firstExpense);
        entity.addExpense(secondExpense);


        sut.mapToDomainModel(entity);


        verify(expenseMapper).mapToDomainModel(firstExpense);
        verify(expenseMapper).mapToDomainModel(secondExpense);
    }
}