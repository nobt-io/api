package io.nobt.config;

import io.pivotal.labs.cfenv.CloudFoundryEnvironment;
import io.pivotal.labs.cfenv.CloudFoundryEnvironmentException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class Config {

    private static final Logger LOGGER = LogManager.getLogger(Config.class);

    public abstract void initialize() throws Exception;

    public abstract int getDatabasePort();

    public abstract DatabaseConfig getDatabaseConfig();

    public static Config getConfigForCurrentEnvironment() {

        final Config config = determineConfigInstance();

        try {
            config.initialize();
        } catch (Exception e) {
            LOGGER.error("Failed to initialize configuration.", e);
            throw new IllegalStateException(e);
        }

        return config;
    }

    private static Config determineConfigInstance() {
        try {
            final CloudFoundryEnvironment environment = new CloudFoundryEnvironment(System::getenv);
            return new RemoteConfig(environment);
        } catch (CloudFoundryEnvironmentException e) {
            return new LocalConfig();
        }
    }
}
