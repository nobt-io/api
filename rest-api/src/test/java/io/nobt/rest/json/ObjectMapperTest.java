package io.nobt.rest.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ObjectMapperTest {

    private ObjectMapper sut;

    @Before
    public void setUp() throws Exception {
        sut = new ObjectMapperFactory().create();
    }

    @Test
    public void shouldSerializeLocalDatesAsISO6801Timestamp() throws Exception {

        final LocalDate someDay = LocalDate.of(2016, 10, 5);

        final String serializedLocalDate = sut.writeValueAsString(someDay);

        assertThat(serializedLocalDate, is("\"2016-10-05\""));
    }

    @Test
    public void shouldSerializeLocalDateTimeAsISO6801Timestamp() throws Exception {

        final LocalDateTime someDay = LocalDateTime.of(2016, 10, 5, 10, 0, 0);

        final String serializedLocalDate = sut.writeValueAsString(someDay);

        assertThat(serializedLocalDate, is("\"2016-10-05T10:00:00\""));
    }
}