package io.nobt.persistence.dao;

import static io.nobt.matchers.ExpenseMatchers.hasDebteeWithName;
import static io.nobt.matchers.ExpenseMatchers.hasDebtorWithName;
import static io.nobt.matchers.ExpenseMatchers.hasNumberOfDebtors;
import static io.nobt.matchers.NobtMatchers.hasExpense;
import static io.nobt.matchers.NobtMatchers.hasExplicitParticipantWithName;
import static io.nobt.matchers.NobtMatchers.hasName;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import io.nobt.core.domain.Nobt;
import io.nobt.persistence.entity.ExpenseEntity;
import io.nobt.persistence.entity.NobtEntity;
import io.nobt.util.Sets;

public class NobtMapperTest {

    private NobtMapper sut;

    @Before
    public void setUp() throws Exception {
        sut = new NobtMapper();
    }

    @Test
    public void shouldMapNobtEntityToNobt() throws Exception {

        final NobtEntity entity = new NobtEntity("Name", Sets.newHashSet("Lukas", "David"));

        final ExpenseEntity billa = new ExpenseEntity("Billa", BigDecimal.ONE, "Thomas");
        billa.addDebtor("Martin");

        entity.addExpense(billa);

        final Nobt nobt = sut.mapNobt(entity);

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