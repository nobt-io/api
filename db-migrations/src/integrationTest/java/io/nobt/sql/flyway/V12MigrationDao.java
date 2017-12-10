package io.nobt.sql.flyway;

import db.migration.v12.V12Person;
import db.migration.v12.V12_1_NobtEntity;
import io.nobt.persistence.DatabaseConfig;

import java.util.Set;

public class V12MigrationDao extends AbstractMigrationDao {

    public V12MigrationDao(DatabaseConfig databaseConfig) {
        super(databaseConfig);
    }

    public Long insertNobtWithParticipants(String participants) {

        final V12_1_NobtEntity entity = new V12_1_NobtEntity();
        entity.setExplicitParticipants_legacy(participants);

        doInTx(entityManager -> {
            entityManager.persist(entity);
            return null;
        });

        return entity.id;
    }

    public Set<V12Person> getParticipantsOfNobt(long id) {
        return doInTx(entityManager -> entityManager
                .createQuery("SELECT n from V12_1_NobtEntity n WHERE n.id = :id", V12_1_NobtEntity.class)
                .setParameter("id", id)
                .getSingleResult()
                .getExplicitParticipants()
        );
    }

}
