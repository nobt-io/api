#!/usr/bin/env bash

docker run --name nobt-io-postgres-db -e POSTGRES_PASSWORD=password -d -p 5432:5432 postgres:9