package io.nobt.rest.encoding;

import java.util.Arrays;

public class ContentTypeCharsetParser {

	public String parseCharset(String contentType) {
		final String[] parts = contentType.split(";");

		final String encodingArgument = parseEncodingArgument(parts);

		final String[] keyAndValue = encodingArgument.split("=");

		if (keyAndValue.length != 2) {
			throw new EncodingNotSpecifiedException();
		}

		return keyAndValue[1];
	}

	private String parseEncodingArgument(String[] parts) {
		return Arrays
				.stream(parts)
				.map(String::trim)
				.filter(part -> part.startsWith("charset"))
				.findFirst()
				.orElseThrow(EncodingNotSpecifiedException::new);
	}
}
