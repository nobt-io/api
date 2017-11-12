package db.migration.v12;

import com.fasterxml.jackson.databind.module.SimpleModule;

public class V12_1_NobtEntityModule extends SimpleModule {
    public V12_1_NobtEntityModule() {
        setMixInAnnotation(V12Person.class, V12PersonMixin.class);
    }
}
