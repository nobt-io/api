package io.nobt.core.domain;

import io.nobt.util.Sets;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static io.nobt.core.domain.test.PersonFactory.*;
import static io.nobt.core.domain.test.ShareFactory.randomShare;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class NobtTest {

    private Nobt sut;

    @Mock
    private Expense firstExpense;

    @Mock
    private Expense secondExpense;

    @Before
    public void setUp() throws Exception {

        sut = new Nobt(null, "Something", Sets.newHashSet(thomas))
                .addExpense(firstExpense)
                .addExpense(secondExpense);

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
}