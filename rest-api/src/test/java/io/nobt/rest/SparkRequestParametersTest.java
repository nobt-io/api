package io.nobt.rest;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import spark.Request;

import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SparkRequestParametersTest {

    private Request requestMock;

    @Before
    public void setUp() throws Exception {
        requestMock = mock(Request.class);
    }

    @Test
    public void shouldReadHostFromRequest() {

        when(requestMock.host()).thenReturn("localhost:8080");
        final SparkRequestParameters sut = new SparkRequestParameters(requestMock, null);

        assertThat(sut.getHost(), Matchers.is("localhost:8080"));
    }

    @Test
    public void givenCustomSchemeFromCustomHeader_shouldOverrideRequestScheme() {

        when(requestMock.headers("X-Custom-Header")).thenReturn("https");
        when(requestMock.scheme()).thenReturn("http");
        final SparkRequestParameters sut = new SparkRequestParameters(requestMock, "X-Custom-Header");

        assertThat(sut.getScheme(), Matchers.is("https"));
    }

    @Test
    public void givenNoCustomSchemeFromCustomHeader_shouldUseRequestScheme() {

        when(requestMock.scheme()).thenReturn("http");
        final SparkRequestParameters sut = new SparkRequestParameters(requestMock, "X-Custom-Header");

        assertThat(sut.getScheme(), Matchers.is("http"));
    }

    @Test
    public void givenNoCustomHeader_shouldUseRequestScheme() {

        when(requestMock.scheme()).thenReturn("http");
        final SparkRequestParameters sut = new SparkRequestParameters(requestMock, null);

        assertThat(sut.getScheme(), Matchers.is("http"));
    }
}