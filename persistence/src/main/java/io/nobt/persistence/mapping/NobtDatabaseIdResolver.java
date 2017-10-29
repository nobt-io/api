package io.nobt.persistence.mapping;

import java.util.Optional;

public interface NobtDatabaseIdResolver {

    Optional<Long> resolveDatabaseId(String externalId);

}
