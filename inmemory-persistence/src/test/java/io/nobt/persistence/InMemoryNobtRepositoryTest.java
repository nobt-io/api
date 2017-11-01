package io.nobt.persistence;

import io.nobt.core.domain.Nobt;
import io.nobt.core.domain.NobtId;
import org.junit.Before;
import org.junit.Test;

import static io.nobt.test.domain.factories.NobtBuilderProvider.aNobt;
import static io.nobt.test.domain.matchers.NobtMatchers.hasId;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class InMemoryNobtRepositoryTest {

    private InMemoryNobtRepository sut;

    @Before
    public void setUp() throws Exception {
        sut = new InMemoryNobtRepository();
    }

    @Test
    public void shouldAssignIdToNobt() throws Exception {

        final Nobt nobtWithoutId = aNobt()
                .withId(null)
                .build();


        final NobtId nobtId = sut.save(nobtWithoutId);


        final Nobt persistedNobt = sut.getById(nobtId);

        assertThat(persistedNobt, hasId(notNullValue(NobtId.class)));
    }
}