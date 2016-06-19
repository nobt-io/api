package io.nobt.rest.json;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectMapperFactory {

    public ObjectMapper create() {
        return new ObjectMapper().registerModules(new CoreModule());
    }
}
