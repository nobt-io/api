package db.migration.v12;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.util.Arrays;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public class Nobt {

    private final String explicitParticipants;
    private long id;
    private Set<V12Person> v13People;

    public Nobt(long id, String explicitParticipants) {
        this.id = id;
        this.explicitParticipants = explicitParticipants;
    }

    public void convert() {

        final String[] names = explicitParticipants.split(";");

        v13People = Arrays.stream(names).filter(name -> !name.isEmpty()).map(V12Person::forName).collect(toSet());
    }

    public long getId() {
        return id;
    }

    public String getExplicitParticipantsAsJSON(ObjectWriter objectWriter) {
        try {
            return objectWriter.writeValueAsString(v13People);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public Set<V12Person> getV13People() {
        return v13People;
    }
}
