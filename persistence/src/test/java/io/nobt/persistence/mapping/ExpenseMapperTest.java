package io.nobt.persistence.mapping;

import io.nobt.persistence.entity.ExpenseEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ExpenseMapperTest {

    private ExpenseMapper sut;

    @Mock
    private ShareMapper shareMapperMock;

    @Before
    public void setUp() throws Exception {
        sut = new ExpenseMapper(shareMapperMock);
    }

    @Test
    public void shouldUseShareMapperToMapShareBytes() throws Exception {

        final ExpenseEntity expenseEntity = new ExpenseEntity();

        final byte[] shareBytes = {1, 0, 1, 1};
        expenseEntity.setShares(shareBytes);


        sut.mapToDomain(expenseEntity);


        verify(shareMapperMock).mapToShareSet(shareBytes);
    }
}