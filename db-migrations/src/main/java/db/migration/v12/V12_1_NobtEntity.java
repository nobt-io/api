package db.migration.v12;

import io.nobt.persistence.JacksonUtil;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Table(name = "nobts")
@Entity
public class V12_1_NobtEntity {

    /**
     * Need value 2 for backwards compatibility with V4.1__insert_data.sql
     */
    private static final int INITIAL_SEQUENCE_VALUE = 2;

    static {
        JacksonUtil.OBJECT_MAPPER.registerModule(new V12_1_NobtEntityModule());
    }

    @Id
    @SequenceGenerator(name = "nobts_seq", sequenceName = "nobts_seq", initialValue = INITIAL_SEQUENCE_VALUE)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "nobts_seq")
    public Long id;

    @Type(type = "io.nobt.persistence.JsonBinaryType")
    @Column(name = "explicitParticipants")
    private Set<V12Person> explicitParticipants;

    @Column(name = "explicitParticipants_legacy")
    private String explicitParticipants_legacy;

    @Column(name = "nobtName", nullable = false, length = 50)
    private String name = "Test";

    @Column(name = "currency", nullable = false, length = 3)
    private String currency = "EUR";

    @Column(name = "createdOn", nullable = false)
    private ZonedDateTime createdOn = ZonedDateTime.now();

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
