package io.nobt.dbconfig.cloud;

import io.nobt.persistence.DatabaseConfig;
import io.pivotal.labs.cfenv.CloudFoundryEnvironment;
import io.pivotal.labs.cfenv.CloudFoundryEnvironmentException;

import java.net.URI;
import java.net.URISyntaxException;

public class CloudDatabaseConfig implements DatabaseConfig {

    private final CFConnectionStringAdapter connectionString;

    private CloudDatabaseConfig(CFConnectionStringAdapter connectionString) {
        this.connectionString = connectionString;
    }

    public static CloudDatabaseConfig create() {

        final CloudFoundryEnvironment cloudFoundryEnvironment = parseEnvironment();
        final URI elephantSqlUri = getUriToElephantSqlService(cloudFoundryEnvironment);

        final CFConnectionStringAdapter connectionStringAdapter = CFConnectionStringAdapter.parse(elephantSqlUri);

        return new CloudDatabaseConfig(connectionStringAdapter);
    }

    private static URI getUriToElephantSqlService(CloudFoundryEnvironment cloudFoundryEnvironment) {
        try {
            return cloudFoundryEnvironment.getService("database").getUri();
        } catch (URISyntaxException e) {
            throw new IllegalStateException(e);
        }
    }

    private static CloudFoundryEnvironment parseEnvironment() {
        try {
            return new CloudFoundryEnvironment(System::getenv);
        } catch (CloudFoundryEnvironmentException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public String url() {
        return connectionString.getUrl();
    }

    @Override
    public String username() {
        return connectionString.getUsername();
    }

    @Override
    public String password() {
        return connectionString.getPassword();
    }
}
