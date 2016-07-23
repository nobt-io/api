# Download & run

You can grab the pre-built package by clicked on the green `passed` button of the build status right above this documentation.

Once you extract the downloaded `artifacts.zip`, navigate down the folder structure and extract the `rest-api-{VERSION}.zip` file.
To run the API, simply run either the Batch- or Shell-Skript in the `bin/` folder.

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

To build the project from source, simply issue the following command: `./gradlew clean build`.
The ready-to-run API can be found under `build/distributions`.

# Profiles

The application is aware of three profiles.

- STANDALONE
- LOCAL
- CLOUD

If no profile is specified (via the environment variable `profile`), the STANDALONE profile is picked. This causes the application to use an In-Memory database instead of trying to connect an SQL database.