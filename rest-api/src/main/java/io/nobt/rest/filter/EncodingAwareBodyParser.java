package io.nobt.rest.filter;

import io.nobt.rest.encoding.ContentTypeCharsetParser;
import spark.Filter;
import spark.Request;
import spark.Response;

public class EncodingAwareBodyParser implements Filter {

	private ContentTypeCharsetParser charsetParser;

	public EncodingAwareBodyParser() {
		charsetParser = new ContentTypeCharsetParser();
	}

	@Override
	public void handle(Request request, Response response) throws Exception {
		if (hasBody(request)) {
			final String charset = charsetParser.parseCharset(request.contentType());
			request.attribute("body", new String(request.bodyAsBytes(), charset));
		}
	}

	private boolean hasBody(Request request) {
		return request.bodyAsBytes().length != 0;
	}
}
