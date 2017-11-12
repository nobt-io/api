package db.migration;

import db.migration.v12.V12Person;
import db.migration.v12.V12_1_NobtEntity;
import io.nobt.sql.flyway.EntityManagerBasedMigrationTestBase;
import io.nobt.util.Sets;
import org.flywaydb.core.api.migration.MigrationChecksumProvider;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class V12_1_1__Migrate_explicit_participants_to_jsonb_Test extends EntityManagerBasedMigrationTestBase<V12_1_NobtEntity> implements MigrationChecksumProvider {

    @Override
    public Integer getChecksum() {
        return V12_1_1__Migrate_explicit_participants_to_jsonb_Test.class.getName().hashCode();
    }

    @Override
    protected void assertState(EntityManager entityManager) {

        final List<V12_1_NobtEntity> nobts = fetchEntities(entityManager);

        assertThat("We want at least 3 instances after the migration", nobts, hasSize(anyOf(
                greaterThan(3),
                equalTo(3)
        )));

        assertThat("Each participant should be an item of the set", getParticipantsOfNobt(nobts, 2), hasSize(5));

        assertThat("Name should now be present in the set", getParticipantsOfNobt(nobts, 3), is(Sets.newHashSet(V12Person.forName("Anna"))));

        assertThat("No participants should result in empty set", getParticipantsOfNobt(nobts, 4), is(empty()));

    }

    private List<V12_1_NobtEntity> fetchEntities(EntityManager entityManager) {
        return entityManager.createQuery("SELECT n from V12_1_NobtEntity n", V12_1_NobtEntity.class).getResultList();
    }

    private Set<V12Person> getParticipantsOfNobt(List<V12_1_NobtEntity> nobts, int id) {
        return findNobtById(nobts, id).getExplicitParticipants();
    }

    private V12_1_NobtEntity findNobtById(List<V12_1_NobtEntity> nobts, int id) {
        return nobts.stream().filter(n -> n.id == id).findFirst().get();
    }
}
