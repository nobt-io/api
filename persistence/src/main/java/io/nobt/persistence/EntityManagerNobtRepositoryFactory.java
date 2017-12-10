package io.nobt.persistence;

import javax.persistence.EntityManager;

public interface EntityManagerNobtRepositoryFactory {
    EntityManagerNobtRepository create(EntityManager entityManager);
}
