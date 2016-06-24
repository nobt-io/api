package io.nobt.persistence.dao;

import io.nobt.core.domain.Nobt;
import io.nobt.persistence.entity.ExpenseEntity;
import io.nobt.persistence.entity.NobtEntity;
import io.nobt.util.Sets;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static io.nobt.matchers.ExpenseMatchers.*;
import static io.nobt.matchers.NobtMatchers.*;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertThat;

public class NobtMapperTest {

    private NobtMapper sut;

    @Before
    public void setUp() throws Exception {
        sut = new NobtMapper();
    }

    @Test
    public void shouldMapNobtEntityToNobt() throws Exception {

        final UUID id = UUID.randomUUID();

        final NobtEntity entity = new NobtEntity("Name", id, Sets.newHashSet("Lukas", "David"));

        final ExpenseEntity billa = new ExpenseEntity("Billa", BigDecimal.ONE, "Thomas");
        billa.addDebtor("Martin");

        entity.addExpense(billa);

        final Nobt nobt = sut.map(entity);

        assertThat(nobt, hasName("Name"));
        assertThat(nobt, allOf(
                hasExplicitParticipantWithName("Lukas"),
                hasExplicitParticipantWithName("David")
        ));
        assertThat(nobt, hasExpense(
                    allOf(
                            hasDebteeWithName("Thomas"),
                            hasDebtorWithName("Martin"),
                            hasNumberOfDebtors(1)
                    )
                )
        );
    }
}