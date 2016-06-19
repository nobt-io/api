package io.nobt.rest.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import spark.ResponseTransformer;

public class JacksonResponseTransformer implements ResponseTransformer {

    private final ObjectMapper objectMapper;

    public JacksonResponseTransformer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public String render(Object o) throws Exception {
        return objectMapper.writeValueAsString(o);
    }
}
