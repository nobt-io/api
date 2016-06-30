# Download & run

You can grab the pre-built package by clicked on the green `passed` button of the build status right above this documentation.

Once you extract the downloaded `artifacts.zip`, navigate down the folder structure and extract the `rest-api-{VERSION}.zip` file.
To run the API, simply run either the Batch- or Shell-Skript in the `bin/` folder. The documentation can be found in the `docs/` folder.

# Building from source

To build the project from source, simply issue the following command: `./gradlew clean build`.
The ready-to-run API can be found under `build/distributions`.

# Profiles

The application is aware of three profiles.

- STANDALONE
- LOCAL
- CLOUD

If no profile is specified (via the environment variable `profile`), the STANDALONE profile is picked. This causes the application to use an In-Memory database instead of trying to connect an SQL database.