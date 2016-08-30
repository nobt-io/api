package io.nobt.sql.flyway;

import io.nobt.dbconfig.cloud.CloudDatabaseConfig;
import io.nobt.dbconfig.local.LocalDatabaseConfig;
import io.nobt.persistence.DatabaseConfig;
import io.nobt.profiles.Profile;
import io.nobt.profiles.Profiles;

import java.util.concurrent.CountDownLatch;

public class MigrationApp {

    public static void main(String[] args) throws InterruptedException {

        final CountDownLatch latch = new CountDownLatch(1);
        final MigrationService migrationService = new MigrationService();

        final DatabaseConfig databaseConfig = Profile.getCurrentProfile().getProfileDependentValue(() -> null, LocalDatabaseConfig::create, CloudDatabaseConfig::create);

        migrationService.migrateDatabaseAt(databaseConfig);

        // do not exit app if running on cloud to prevent CF from signaling a "crashed" app
        Profiles.ifProfile(Profiles::notCloud, latch::countDown);

        latch.await();
    }
}
