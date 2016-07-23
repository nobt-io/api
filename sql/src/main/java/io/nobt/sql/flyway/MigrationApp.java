package io.nobt.sql.flyway;

import java.util.concurrent.CountDownLatch;

import org.flywaydb.core.Flyway;

import io.nobt.dbconfig.CloudDatabaseConfig;
import io.nobt.dbconfig.DatabaseConfig;
import io.nobt.dbconfig.LocalDatabaseConfig;
import io.nobt.profiles.Profile;
import io.nobt.profiles.Profiles;

public class MigrationApp {

    public static void main(String[] args) throws InterruptedException {

        final CountDownLatch latch = new CountDownLatch(1);

        final DatabaseConfig databaseConfig = Profile.getCurrentProfile().getProfileDependentValue(() -> null, LocalDatabaseConfig::create, CloudDatabaseConfig::create);

        Flyway flyway = new Flyway();
        flyway.setDataSource(databaseConfig.url(), databaseConfig.username(), databaseConfig.password());

        flyway.migrate();

        // do not exit app if running on cloud to prevent CF from signaling a "crashed" app
        Profiles.ifProfile( Profiles::notCloud, latch::countDown);

        latch.await();
    }
}
