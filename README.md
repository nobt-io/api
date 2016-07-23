# Download & run

You can grab the pre-built package by clicked on the green `passed` button of the build status right above this documentation.

Once you extract the downloaded `artifacts.zip`, navigate down the folder structure and extract the `rest-api-{VERSION}.zip` file.
To run the API, simply run either the Batch- or Shell-Skript in the `bin/` folder.

## Profiles

Upon startup, the application chooses one of three profiles:

- __STANDALONE__ (default)
    - PORT 8080
    - In-Memory database
- __LOCAL__
    - PORT 8080
    - PostgreSQL database at `localhost:5432` with `postgres:password` credentials
- __CLOUD__
    - PORT specified by the PORT env variable
    - PostgreSQL database specified by the `elephant-sql` service. 
   
- For all none-database related development (frontend, business-logic) the `STANDALONE` profile should fulfill all your needs. 
- If you mess with the database, you will probably want to test your changes against a local database in which case the `LOCAL` profile comes in handy. (And the `create-docker-db-container.sh` script in the `/bin` folder which provisions a docker container with an up and running postgres database.)
- Running the application in the `CLOUD` profile yourself does not make much sense, as you would have to emulate a cloudfoundry environment. (Which is a none-trivial task.)

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