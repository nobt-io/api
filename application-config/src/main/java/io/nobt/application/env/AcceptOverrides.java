package io.nobt.application.env;

import io.nobt.persistence.DatabaseConfig;

public interface AcceptOverrides extends AcceptEnvironment, BuildConfig {

    AcceptOverrides overridePort(int port);

    AcceptOverrides overrideUseInMemoryDatabase(boolean useInMemoryDatabase);

    AcceptOverrides overrideMigrateDatabaseAtStartup(boolean migrateDatabaseAtStartup);

    AcceptOverrides overrideDatabase(DatabaseConfig databaseConfig);

}
