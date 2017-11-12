package db.migration.v12;

import db.migration.EntityManagerMigration;
import io.nobt.util.Sets;
import org.flywaydb.core.api.migration.MigrationChecksumProvider;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Set;

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

    @Override
    protected void assertState(List<V12_1_NobtEntity> nobts) {

        // Did we not loose any instances?
        Assert.state(nobts.size() >= 3);

        // Did the conversion work?
        Assert.state(getParticipantsOfNobt(nobts, 2).size() == 5);

        // No participants should result in empty array
        Assert.state(getParticipantsOfNobt(nobts, 4).size() == 0);

        // Did we actually convert the name?
        Assert.state(getParticipantsOfNobt(nobts, 3).equals(Sets.newHashSet(V12Person.forName("Anna"))));
    }

    private Set<V12Person> getParticipantsOfNobt(List<V12_1_NobtEntity> nobts, int id) {
        return findNobtById(nobts, id).getExplicitParticipants();
    }

    private V12_1_NobtEntity findNobtById(List<V12_1_NobtEntity> nobts, int id) {
        return nobts.stream().filter(n -> n.id == id).findFirst().get();
    }
}
