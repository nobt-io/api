package io.nobt.rest.docs;

import org.springframework.http.HttpHeaders;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Implementation mostly taken from {@link org.springframework.restdocs.operation.preprocess.PatternReplacingContentModifier}.
 */
class PatternHttpHeaderValueReplacingModifier implements HeaderModifier {

    private final Pattern headerKeyPattern;
    private final String replacement;

    public PatternHttpHeaderValueReplacingModifier(Pattern headerKeyPattern, String replacement) {
        this.headerKeyPattern = headerKeyPattern;
        this.replacement = replacement;
    }

    @Override
    public HttpHeaders modify(HttpHeaders httpHeaders) {

        final HttpHeaders newHttpHeaders = new HttpHeaders();

        for (String headerKey : httpHeaders.keySet()) {

            for (String headerValue : httpHeaders.get(headerKey)) {
                Matcher matcher = this.headerKeyPattern.matcher(headerValue);

                if (matcher.matches()) {
                    matcher.reset();
                    StringBuilder builder = new StringBuilder();
                    int previous = 0;
                    while (matcher.find()) {
                        String prefix;
                        if (matcher.groupCount() > 0) {
                            prefix = headerValue.substring(previous, matcher.start(1));
                            previous = matcher.end(1);
                        } else {
                            prefix = headerValue.substring(previous, matcher.start());
                            previous = matcher.end();
                        }
                        builder.append(prefix);
                        builder.append(this.replacement);
                    }
                    if (previous < headerValue.length()) {
                        builder.append(headerValue.substring(previous));
                    }

                    newHttpHeaders.set(headerKey, builder.toString());
                } else {
                    newHttpHeaders.set(headerKey, headerValue);
                }
            }
        }

        return newHttpHeaders;
    }
}
