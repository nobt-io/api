package db.migration.v12_1_1;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import db.migration.v12.V12Person;

public abstract class V12PersonMixin extends V12Person {

    @JsonCreator
    public V12PersonMixin(@JsonProperty("name") String name) {
        super(name);
    }
}
