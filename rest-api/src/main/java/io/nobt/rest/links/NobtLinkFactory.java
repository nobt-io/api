package io.nobt.rest.links;

import io.nobt.core.domain.Nobt;

import java.net.URI;

public class NobtLinkFactory {

    private BasePath basePath;

    public NobtLinkFactory(BasePath basePath) {
        this.basePath = basePath;
    }

    public URI createLinkToNobt(Nobt nobt) {
        return URI.create(String.format("%s/nobts/%s", basePath.asString(), nobt.getId().getValue()));
    }
}
