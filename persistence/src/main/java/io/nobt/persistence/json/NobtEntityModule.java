package io.nobt.persistence.json;

import com.fasterxml.jackson.databind.module.SimpleModule;
import io.nobt.core.domain.Person;

public class NobtEntityModule extends SimpleModule {

    public NobtEntityModule() {
        setMixInAnnotation(Person.class, PersonMixin.class);
    }
}
