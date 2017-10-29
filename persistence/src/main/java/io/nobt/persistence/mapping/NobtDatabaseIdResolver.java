package io.nobt.persistence.mapping;

public interface NobtDatabaseIdResolver {

    Long resolveDatabaseId(String externalId);

}
