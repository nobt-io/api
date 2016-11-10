package io.nobt.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.nobt.rest.json.CoreModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.zalando.problem.ProblemModule;

public class ObjectMapperFactory {

    public ObjectMapper create() {
        return new ObjectMapper()
                .registerModules(new CoreModule())
                .registerModule(new JavaTimeModule())
                .registerModule(new ProblemModule());
    }
}
