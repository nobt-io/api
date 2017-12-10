package db.migration.v15_2;

import db.migration.Migratable;

import javax.persistence.*;

@Table(name = "nobts")
@Entity
public class V15_2_NobtEntity implements Migratable {

    @Id
    @SequenceGenerator(name = "nobts_seq", sequenceName = "nobts_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "externalId", length = 20, nullable = false, unique = true)
    private String externalId;

    @Override
    public void migrate() {
        externalId = ShortURL.encode(PseudoCrypter.pseudoCryptLong(id));
    }
}
