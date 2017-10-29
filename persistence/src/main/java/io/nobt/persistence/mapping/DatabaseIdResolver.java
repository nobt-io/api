package io.nobt.persistence.mapping;

public interface DatabaseIdResolver {

    Long resolveDatabaseId(String externalId);

}
