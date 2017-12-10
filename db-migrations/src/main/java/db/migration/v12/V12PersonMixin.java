package db.migration.v12;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class V12PersonMixin extends V12Person {

    @JsonCreator
    public V12PersonMixin(@JsonProperty("name") String name) {
        super(name);
    }
}
