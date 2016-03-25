package io.nobt.rest.encoding;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;

public class ContentTypeCharsetParserTest {

	private ContentTypeCharsetParser sut;

	@Before
	public void setUp() throws Exception {
		sut = new ContentTypeCharsetParser();
	}

	@Test
	public void testShouldParseCharset() throws Exception {
		final String charset = sut.parseCharset("text/plain; charset=ISO-8859-1");

		Assert.assertThat(charset, is("ISO-8859-1"));
	}
}