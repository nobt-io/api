package io.nobt.persistence;

import io.nobt.core.UnknownNobtException;
import io.nobt.core.domain.Nobt;
import io.nobt.core.domain.NobtId;
import io.nobt.persistence.nobt.NobtEntity;

import javax.persistence.EntityManager;
import java.io.Closeable;
import java.io.IOException;
import java.util.Optional;

public class EntityManagerNobtRepository implements NobtRepository, Closeable {

    private final EntityManager em;
    private final DomainModelMapper<NobtEntity, Nobt> nobtMapper;

    public EntityManagerNobtRepository(EntityManager em, DomainModelMapper<NobtEntity, Nobt> nobtMapper) {
        this.em = em;
        this.nobtMapper = nobtMapper;
    }

    @Override
    public NobtId save(Nobt nobt) {

        final NobtEntity entityToPersist = nobtMapper.mapToDatabaseModel(nobt);

        final NobtEntity persistedEntity = persistOrMerge(entityToPersist);

        return new NobtId(persistedEntity.getId());
    }

    private NobtEntity persistOrMerge(NobtEntity entity) {
        if (entity.getId() == null) {
            em.persist(entity);

            return entity;
        } else {
            return em.merge(entity);
        }
    }

    @Override
    public Nobt getById(NobtId id) {

        final Optional<NobtEntity> nobt = Optional.ofNullable(em.find(NobtEntity.class, id.getId()));

        return nobt.map(nobtMapper::mapToDomainModel).orElseThrow(UnknownNobtException::new);
    }

    @Override
    public void close() throws IOException {
        em.close();
    }
}
