package io.nobt.core.domain;

import io.nobt.test.domain.matchers.NobtMatchers;
import io.nobt.util.Sets;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static io.nobt.test.domain.factories.ShareFactory.randomShare;
import static io.nobt.test.domain.factories.StaticPersonFactory.*;
import static io.nobt.test.domain.matchers.NobtMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
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

        sut = new Nobt(null, "Something", Sets.newHashSet(thomas), Sets.newHashSet(firstExpense, secondExpense), LocalDateTime.now(ZoneOffset.UTC));

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

    @Test
    public void shouldRemoveExpenseById() throws Exception {

        when(firstExpense.getId()).thenReturn(1L);
        when(secondExpense.getId()).thenReturn(2L);

        sut.removeExpense(1L);

        assertThat(sut, hasExpenses(iterableWithSize(1)));
    }
}