# Download

The latest pre-built package is available [here](https://gitlab.com/nobt-io/api/builds/artifacts/master/file/rest-api/build/distributions/rest-api.zip?job=build_rest-api).

# Run

To run the API, simply run either the `run-minimal` Batch- or Shell-Skript in the `bin/` folder.

# Configuration

All configuration values have to be set as environment variables. Take a look at the `run-minimal`-scripts on how to do that.

Available configuration values:

| NAME | TYPE | DESCRIPTION | DEFAULT VALUE |
|------|------|-------------|---------------|
|PORT|integer|Defines the port the API will try to listen on.| - |
|USE_IN_MEMORY_DATABASE|bool|Subtitutes the actual datastore with a non-persistent hashtable.|false|
|MIGRATE_DATABASE_AT_STARTUP|bool|Migrates the defined database to the latest version. Incompatible with `USE_IN_MEMORY_DATABASE`.|false|
|DATABASE_CONNECTION_STRING|string|The connection string the application should use to connect to a database.|-|

# Documentation

## General

There is a general documentation about the API which describes the endpoints and their functionality. You can find it under the `/docs` folder of the artifact. 

## Postman

[![Run in Postman](https://run.pstmn.io/button.svg)](https://app.getpostman.com/run-collection/e290f490661a932c81e9#?env%5BLocal%5D=W3sia2V5IjoiYmFzZSIsInR5cGUiOiJ0ZXh0IiwidmFsdWUiOiJodHRwOi8vbG9jYWxob3N0OjgwODAiLCJlbmFibGVkIjp0cnVlfSx7ImtleSI6ImN1cnJlbnRfbm9idF9sb2NhdGlvbiIsInR5cGUiOiJ0ZXh0IiwidmFsdWUiOiJodHRwOi8vbG9jYWxob3N0OjgwODAvbm9idHMvM1h0QzhNUjJwNlJCIiwiZW5hYmxlZCI6dHJ1ZX1d)

The collection is always up-to-date with the current version of the master branch. (Which is usually up-to-date with the version deployed at the dev-space.)  
It comes with a preconfigured __environment__ (named __Local__) that already defines the necessary properties for testing a locally running API instance. 

The collection automatically remembers the last `nobt` you created and reuses its URL for subsequent requests to retrieve it or create expenses. This means you will almost never have to mess with the URLs of the requests.

# Building from source

To build the project from source, simply issue the following command: `./gradlew clean build`.
This runs all the tests and packages the API into a shippable bundle.

The ready-to-run API can be found under `build/distributions`.

## Testing

Unit tests can be run with the `test` command: `./gradlew test` 

## Integration tests

Docker has to be installed and running in order to execute the integration tests.