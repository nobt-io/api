package io.nobt.dbconfig.test;

public class PortParameterizablePostgresDatabaseConfig extends DefaultPostgresDatabaseConfig {

    private final int port;

    public PortParameterizablePostgresDatabaseConfig(int port) {
        this.port = port;
    }

    @Override
    public Integer port() {
        return port;
    }
}
