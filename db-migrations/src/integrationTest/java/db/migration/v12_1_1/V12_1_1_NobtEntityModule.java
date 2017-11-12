package db.migration.v12_1_1;

import com.fasterxml.jackson.databind.module.SimpleModule;
import db.migration.v12.V12Person;

public class V12_1_1_NobtEntityModule extends SimpleModule {

    public V12_1_1_NobtEntityModule() {
        setMixInAnnotation(V12Person.class, V12PersonMixin.class);
    }
}
