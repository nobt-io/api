package io.nobt.rest.config;

import javax.persistence.EntityManagerFactory;

import io.pivotal.labs.cfenv.CloudFoundryEnvironment;
import io.pivotal.labs.cfenv.CloudFoundryEnvironmentException;

public abstract class Config {

    public abstract int getPort();

	public abstract EntityManagerFactory getEntityManagerFactory();

    public static Config getConfigForCurrentEnvironment() {

        try {
            final CloudFoundryEnvironment environment = new CloudFoundryEnvironment(System::getenv);
            return new RemoteConfig(environment);
        } catch (CloudFoundryEnvironmentException e) {
            return new LocalConfig();
        }
    }
}
