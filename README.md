# Documentation

The API is documented with Postman. The current documentation can be found [here](https://www.getpostman.com/collections/ec77787753c3ff915a63).

The documentation also comes with integration tests for the API.

# Database

The system tries to a postgres database on startup. Depending on the environment it is running, the connection string is either parsed from a properties file (local) or from environment variables (CloudFoundry).

For more information, look at the config module.