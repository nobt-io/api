#!/usr/bin/env bash

./gradlew :sql:dZ

cf bind-service migrations-app elephant-sql
cf push migrations-app --no-hostname --no-route --no-manifest --health-check-type -b java_buildpack -p sql/build/distributions/*.zip