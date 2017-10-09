package io.nobt.persistence;

public interface DomainModelMapper<DBM, DM> {

    DM mapToDomainModel(DBM databaseModel);

    DBM mapToDatabaseModel(DM domainModel);
}
