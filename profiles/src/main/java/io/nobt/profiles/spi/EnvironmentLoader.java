package io.nobt.profiles.spi;

import io.nobt.profiles.Environment;

import java.util.ServiceLoader;
import java.util.stream.StreamSupport;

public class EnvironmentLoader {

    public static Environment load() {
        return StreamSupport
                .stream(ServiceLoader.load(Environment.class).spliterator(), false)
                .findFirst()
                .orElseThrow( () -> new IllegalStateException("No environment could be loaded.") );
    }
}
