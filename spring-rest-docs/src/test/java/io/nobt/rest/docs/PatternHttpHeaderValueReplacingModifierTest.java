package io.nobt.rest.docs;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.regex.Pattern;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class PatternHttpHeaderValueReplacingModifierTest {

    private PatternHttpHeaderValueReplacingModifier sut;

    @Before
    public void setUp() throws Exception {
        sut = new PatternHttpHeaderValueReplacingModifier(Pattern.compile("(http://localhost:8080)/.*"), "http://api.nobt.io");
    }

    @Test
    public void shouldReplaceHeaderBasedOnPattern() throws Exception {

        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Location", "http://localhost:8080/nobts");


        final HttpHeaders modified = sut.modify(httpHeaders);


        assertThat(modified.get("Location"), hasItem("http://api.nobt.io/nobts"));
    }

    @Test
    public void shouldNotTouchNonMatchingHeaders() throws Exception {

        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);


        final HttpHeaders modified = sut.modify(httpHeaders);


        assertThat(modified.getContentType(), is(MediaType.APPLICATION_JSON));
    }
}