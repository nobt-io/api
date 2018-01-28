package io.nobt.rest.links;

import java.net.URI;

public interface LinkFactory<T> {
    URI createLinkTo(T t);
}
