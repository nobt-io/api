package io.nobt.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.nobt.rest.json.CoreModule;
import org.zalando.problem.ProblemModule;

public class ObjectMapperFactory {

    public ObjectMapper create() {
        return new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .registerModule(new ProblemModule())
                .registerModule(new Jdk8Module())
                .registerModules(new CoreModule());
    }
}
