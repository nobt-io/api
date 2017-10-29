package io.nobt.core.domain;

import io.nobt.util.Sets;
import org.junit.Test;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static io.nobt.test.domain.Currencies.EUR;
import static io.nobt.test.domain.factories.StaticPersonFactory.thomas;
import static io.nobt.test.domain.matchers.NobtMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class NobtFactoryTest {

    @Test
    public void shouldCreateNobtWithDefaultValues() throws Exception {

        NobtFactory sut = new NobtFactory(Clock.fixed(LocalDateTime.of(2017, 1, 1, 0, 0, 0).toInstant(ZoneOffset.UTC), ZoneOffset.UTC));


        Nobt nobt = sut.create("Test", Sets.newHashSet(thomas), EUR);


        assertThat(nobt, allOf(
                hasName(equalTo("Test")),
                hasCurrency(equalTo(EUR)),
                hasParticipants(containsInAnyOrder(thomas)),
                hasId(notNullValue(NobtId.class)),
                hasCreationTime(equalTo(ZonedDateTime.of(2017, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC)))
        ));
    }
}