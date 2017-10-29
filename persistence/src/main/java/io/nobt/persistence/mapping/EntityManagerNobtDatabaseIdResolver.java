package io.nobt.persistence.mapping;

import io.nobt.persistence.entity.NobtEntity;

import javax.persistence.EntityManager;
import java.util.Optional;

public class EntityManagerNobtDatabaseIdResolver implements NobtDatabaseIdResolver {

    private final EntityManager entityManager;

    public EntityManagerNobtDatabaseIdResolver(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<Long> resolveDatabaseId(String externalId) {
        return entityManager
                .createNamedQuery("getByExternalId", NobtEntity.class)
                .setParameter("externalId", externalId)
                .getResultList()
                .stream()
                .findFirst()
                .map(NobtEntity::getId);
    }
}
