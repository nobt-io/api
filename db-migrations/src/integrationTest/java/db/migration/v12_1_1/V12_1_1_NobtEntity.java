package db.migration.v12_1_1;

import db.migration.v12.V12Person;
import io.nobt.persistence.JacksonUtil;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Table(name = "nobts")
@Entity
public class V12_1_1_NobtEntity {

    static {
        JacksonUtil.OBJECT_MAPPER.registerModule(new V12_1_1_NobtEntityModule());
    }

    @Id
    @SequenceGenerator(name = "nobts_seq", sequenceName = "nobts_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Type(type = "io.nobt.persistence.JsonBinaryType")
    @Column(name = "explicitParticipants")
    private Set<V12Person> explicitParticipants = new HashSet<>();

    public Long getId() {
        return id;
    }

    public Set<V12Person> getExplicitParticipants() {
        return explicitParticipants;
    }
}
