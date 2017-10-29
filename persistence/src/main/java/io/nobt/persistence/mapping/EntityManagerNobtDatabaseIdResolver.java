package io.nobt.persistence.mapping;

import io.nobt.persistence.entity.NobtEntity;

import javax.persistence.EntityManager;

public class EntityManagerNobtDatabaseIdResolver implements NobtDatabaseIdResolver {

    private final EntityManager entityManager;

    public EntityManagerNobtDatabaseIdResolver(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Long resolveDatabaseId(String externalId) {
        final NobtEntity nobtEntity = entityManager
                .createNamedQuery("getByExternalId", NobtEntity.class)
                .setParameter("externalId", externalId)
                .getSingleResult();

        return nobtEntity.getId();
    }
}
