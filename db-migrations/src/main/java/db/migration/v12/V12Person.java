package db.migration.v12;

import java.io.Serializable;
import java.util.Objects;

public class V12Person implements Serializable {

    private String name;

    public V12Person(String name) {
        this.name = name;
    }

    public static V12Person forName(String name) {
        return new V12Person(name);
    }

    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof V12Person)) return false;
        V12Person v12Person = (V12Person) o;
        return Objects.equals(name, v12Person.name);
    }

    @Override
    public String toString() {
        return name;
    }
}
