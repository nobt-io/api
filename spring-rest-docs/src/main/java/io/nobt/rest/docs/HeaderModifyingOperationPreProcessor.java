package io.nobt.rest.docs;

import org.springframework.restdocs.operation.OperationRequest;
import org.springframework.restdocs.operation.OperationRequestFactory;
import org.springframework.restdocs.operation.OperationResponse;
import org.springframework.restdocs.operation.OperationResponseFactory;
import org.springframework.restdocs.operation.preprocess.OperationPreprocessor;

/**
 * Implementation mostly taken from {@link org.springframework.restdocs.operation.preprocess.ContentModifyingOperationPreprocessor}
 */
class HeaderModifyingOperationPreProcessor implements OperationPreprocessor {

    private final OperationRequestFactory requestFactory = new OperationRequestFactory();
    private final OperationResponseFactory responseFactory = new OperationResponseFactory();

    private final HeaderModifier headerModifier;

    public HeaderModifyingOperationPreProcessor(HeaderModifier headerModifier) {
        this.headerModifier = headerModifier;
    }

    @Override
    public OperationRequest preprocess(OperationRequest request) {
        return this.requestFactory.createFrom(request, headerModifier.modify(request.getHeaders()));
    }

    @Override
    public OperationResponse preprocess(OperationResponse response) {
        return this.responseFactory.createFrom(response, headerModifier.modify(response.getHeaders()));
    }
}
