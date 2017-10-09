package io.nobt.persistence;

import io.nobt.core.domain.CurrencyKey;
import io.nobt.core.domain.Nobt;
import io.nobt.core.domain.NobtFactory;
import io.nobt.core.domain.NobtId;
import io.nobt.test.domain.factories.StaticPersonFactory;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Collections;

import static io.nobt.test.domain.matchers.NobtMatchers.hasId;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class InMemoryNobtRepositoryTest {

    private NobtFactory nobtFactory;
    private InMemoryNobtRepository sut;

    @Before
    public void setUp() throws Exception {

        nobtFactory = new NobtFactory();

        sut = new InMemoryNobtRepository();
    }

    @Test
    public void shouldAssignIdToNobtAndExpenses() throws Exception {

        final Nobt nobtWithoutId = nobtFactory.create("Test", Collections.emptySet(), new CurrencyKey("EUR"));
        nobtWithoutId.addExpense("Something", "RANDOM", StaticPersonFactory.david, Collections.emptySet(), LocalDate.now(), null);

        final NobtId nobtId = sut.save(nobtWithoutId);

        final Nobt persistedNobt = sut.getById(nobtId);

        assertThat(persistedNobt, hasId(notNullValue(NobtId.class)));
    }
}