package db.migration.v12;

import db.migration.EntityManagerMigration;
import org.flywaydb.core.api.migration.MigrationChecksumProvider;

import javax.persistence.EntityManager;
import java.util.List;

public class V12_1__Migrate_explicit_participants_to_jsonb extends EntityManagerMigration<V12_1_NobtEntity> implements MigrationChecksumProvider {

    @Override
    public Integer getChecksum() {
        return V12_1__Migrate_explicit_participants_to_jsonb.class.getName().hashCode();
    }

    @Override
    protected List<V12_1_NobtEntity> retrieveEntities(EntityManager entityManager) {
        return entityManager.createQuery("SELECT n from V12_1_NobtEntity n", V12_1_NobtEntity.class).getResultList();
    }

    @Override
    protected V12_1_NobtEntity performUpdate(V12_1_NobtEntity entity) {

        entity.convert();

        return entity;
    }

}
