package io.nobt.rest.docs;

import org.springframework.restdocs.operation.preprocess.OperationPreprocessor;

import java.util.regex.Pattern;

public final class Preprocessors {

    public static OperationPreprocessor replacePatternInHeader(Pattern pattern,
                                                               String replacement) {
        return new HeaderModifyingOperationPreProcessor(new PatternHttpHeaderValueReplacingModifier(pattern, replacement));
    }

}
