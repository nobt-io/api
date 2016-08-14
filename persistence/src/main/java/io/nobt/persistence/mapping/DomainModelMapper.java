package io.nobt.persistence.mapping;

public interface DomainModelMapper<DBM, DM> {

    DM mapToDomainModel(DBM databaseModel);

    DBM mapToDatabaseModel(DM domainModel);
}
