# Nobt.io API

[![CI](https://github.com/nobt-io/api/actions/workflows/ci.yml/badge.svg)](https://github.com/nobt-io/api/actions/workflows/ci.yml)
[![Dependabot Status](https://api.dependabot.com/badges/status?host=github&repo=nobt-io/api)](https://dependabot.com)

## Download

The latest pre-built package is available [here](https://gitlab.com/nobt-io/api/builds/artifacts/master/file/rest-api/build/distributions/rest-api.zip?job=build_rest-api).

## Run

To run the API, simply run either the `run-minimal` Batch- or Shell-Skript in the `bin/` folder.

## Configuration

All configuration values have to be set as environment variables. Take a look at the `run-minimal`-scripts on how to do that.

Available configuration values:

| NAME | TYPE | DESCRIPTION | DEFAULT VALUE |
|------|------|-------------|---------------|
|PORT|integer|Defines the port the API will try to listen on.| - |
|USE_IN_MEMORY_DATABASE|bool|Subtitutes the actual datastore with a non-persistent hashtable.|false|
|MIGRATE_DATABASE_AT_STARTUP|bool|Migrates the defined database to the latest version. Incompatible with `USE_IN_MEMORY_DATABASE`.|false|
|DATABASE_CONNECTION_STRING|string|The connection string the application should use to connect to a database.|-|
|SCHEME_OVERRIDE_HEADER|string|The HTTP-Header that the API uses to override the scheme of generated links, if present.|-|

## Documentation

### General

There is a general documentation about the API which describes the endpoints and their functionality. You can find it under the `/docs` folder of the artifact. 

## Building from source

To build the project from source, simply issue the following command: `./gradlew clean build`.
This runs all the tests and packages the API into a shippable bundle.

The ready-to-run API can be found under `build/distributions`.

### Testing

Unit tests can be run with the `test` command: `./gradlew test` 

### Integration tests

Docker has to be installed and running in order to execute the integration tests.
