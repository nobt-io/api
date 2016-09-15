# Download & run

You can grab the pre-built package by clicked on the green `passed` button of the build status right above this documentation.

Once you extract the downloaded `artifacts.zip`, navigate down the folder structure and extract the `rest-api-{VERSION}.zip` file.
To run the API, simply run either the `run-minimal` Batch- or Shell-Skript in the `bin/` folder.

## The application can be configured with a number of switches:

All configuration values have to be set as environment variables. Take a look at the `run-minimal`-scripts on how to do that.

Available configuration values:

| NAME | TYPE | DESCRIPTION | DEFAULT VALUE |
|------|------|-------------|---------------|
|PORT|integer|Defines the port the API will try to listen on.| - |
|REPORT_SERVER_ERRORS_AS_ISSUES|bool|Reports uncaught exceptions (e.g. those resulting in an 500) as issues to our [issue-tracker](gitlab.com/nobt-io/api/issues).|false|
|WRITE_STACKTRACE_TO_RESPONSE|bool|Prints the stacktrace to the response.|true|
|USE_IN_MEMORY_DATABASE|bool|Subtitutes the actual datastore with a non-persistent hashtable.|false|
|MIGRATE_DATABASE_AT_STARTUP|bool|Migrates the defines database to the latest version. Incompatible with `USE_IN_MEMORY_DATABASE`.|false|
|DATABASE_CONNECTION_STRING|string|The connection string the application should use to connect to a database.|-|

# Documentation

## General

There is a general documentation about the API which describes the endpoints and their functionality. You can find it under the `/docs` folder of the artifact. 

## Postman

[![Run in Postman](https://run.pstmn.io/button.svg)](https://app.getpostman.com/run-collection/d301df6b78706da96698)

In order to use the Postman collection, you have to define an __environment__ with the following properties:
 
| Property | Content |
|----------|---------|
| base | The base URL of the API deployment you want to send the requests against. Usually, you will want to configure a locally running instance here in which case you would configure the base as `http://localhost:8080`.

The collection automatically remembers the last `nobt` you created and reuses its URL for subsequent requests to retrieve it or create expenses. This means you will almost never have to mess with the URLs of the requests.

# Building from source

To build the project from source, simply issue the following command: `./gradlew distZip`.
The ready-to-run API can be found under `build/distributions`.

The `distZip` command performs only the really necessary tasks needed for building the zip-file.

## Testing

Unit tests can be run with the `test` command: `./gradlew test -x integrationTest`  
Note that the command above skips the integration tests because these need a running postgres database.

## Integration tests

To run all integration tests locally, use the batch-file located in the root folder. For this to work you need to have docker installed and running.