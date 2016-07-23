#!/usr/bin/env bash

./gradlew clean :sql:dZ

cf push -f sql/manifest.yml -p sql/build/distributions/*.zip