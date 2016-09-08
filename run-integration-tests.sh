#!/usr/bin/env bash

CONTAINER_ID=$(docker run -d -e POSTGRES_PASSWORD=password -p 5432:5432 postgres:9)

echo "Running integration tests against $CONTAINER_ID."

./gradlew clean integrationTest -Dorg.gradle.parallel=false

docker rm -f $CONTAINER_ID > /dev/nul