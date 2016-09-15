package io.nobt.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.nobt.rest.json.CoreModule;

public class ObjectMapperFactory {

    public ObjectMapper create() {
        return new ObjectMapper().registerModules(new CoreModule());
    }
}
