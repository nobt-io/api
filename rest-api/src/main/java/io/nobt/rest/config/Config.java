package io.nobt.rest.config;

public abstract class Config {

    public abstract int getPort();

    public static Config getConfigForCurrentEnvironment() {
        if (isCFEnvironment()) {
            return new RemoteConfig();
        } else {
            return new LocalConfig();
        }
    }

    private static boolean isCFEnvironment() {
        return System.getenv("VCAP_APPLICATION") != null;
    }
}
