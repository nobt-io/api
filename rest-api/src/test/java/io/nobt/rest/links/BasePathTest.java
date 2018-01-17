package io.nobt.rest.links;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import spark.Request;

import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BasePathTest {

    private Request requestMock;

    @Before
    public void setUp() throws Exception {
        requestMock = mock(Request.class);
    }

    @Test
    public void shouldReadHostFromRequest() {

        when(requestMock.host()).thenReturn("localhost:8080");
        when(requestMock.scheme()).thenReturn("http");

        final BasePath basePath = BasePath.parse(requestMock, null);

        assertThat(basePath.asString(), Matchers.is("http://localhost:8080"));
    }

    @Test
    public void givenCustomSchemeFromCustomHeader_shouldOverrideRequestScheme() {

        when(requestMock.host()).thenReturn("localhost:8080");
        when(requestMock.headers("X-Custom-Header")).thenReturn("https");
        when(requestMock.scheme()).thenReturn("http");

        final BasePath basePath = BasePath.parse(requestMock, "X-Custom-Header");

        assertThat(basePath.asString(), Matchers.is("https://localhost:8080"));
    }

    @Test
    public void givenNoCustomSchemeFromCustomHeader_shouldUseRequestScheme() {

        when(requestMock.host()).thenReturn("localhost:8080");
        when(requestMock.scheme()).thenReturn("http");

        final BasePath basePath = BasePath.parse(requestMock, "X-Custom-Header");

        assertThat(basePath.asString(), Matchers.is("http://localhost:8080"));
    }
}