package io.nobt.rest.docs;

import org.springframework.http.HttpHeaders;

interface HeaderModifier {

    HttpHeaders modify(HttpHeaders httpHeaders);

}
