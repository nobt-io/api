package db.migration.v12;

import com.fasterxml.jackson.databind.module.SimpleModule;
import io.nobt.persistence.JacksonUtil;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Table(name = "nobts")
@Entity
public class V12_1_NobtEntity {

    static {
        JacksonUtil.OBJECT_MAPPER.registerModule(new SimpleModule() {
            @Override
            public void setupModule(SetupContext context) {
                setMixInAnnotation(V12Person.class, V12PersonMixin.class);
            }
        });
    }

    @Id
    @SequenceGenerator(name = "nobts_seq", sequenceName = "nobts_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Type(type = "io.nobt.persistence.JsonBinaryType")
    @Column(name = "explicitParticipants")
    private Set<V12Person> explicitParticipants;

    @Column(name = "explicitParticipants_legacy")
    private String explicitParticipants_legacy;

    public void convert() {

        final String[] names = explicitParticipants_legacy.split(";");

        explicitParticipants = Arrays.stream(names).filter(name -> !name.isEmpty()).map(V12Person::forName).collect(toSet());
    }

    public Set<V12Person> getExplicitParticipants() {
        return explicitParticipants;
    }

    public void setExplicitParticipants(Set<V12Person> explicitParticipants) {
        this.explicitParticipants = explicitParticipants;
    }

    public String getExplicitParticipants_legacy() {
        return explicitParticipants_legacy;
    }

    public void setExplicitParticipants_legacy(String explicitParticipants_legacy) {
        this.explicitParticipants_legacy = explicitParticipants_legacy;
    }
}
