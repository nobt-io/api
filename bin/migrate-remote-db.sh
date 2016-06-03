#!/usr/bin/env bash

./gradlew :sql:dZ

cf push -f sql/manifest.yml -p sql/build/distributions/*.zip