#!/usr/bin/env bash

./gradlew :sql:dZ

cf bind-service migrations-app elephant-sql
cf push migrations-app --no-hostname --no-route --no-manifest --health-check-type none -m 128MB -b java_buildpack -p sql/build/distributions/*.zip
# cf stop migrations-app # TODO otherwise CF keeps restarting the app because it is detected as "crash" as exits as soon as the migration is finished