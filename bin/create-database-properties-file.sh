#!/usr/bin/env bash

CONFIG_FILE=config/src/main/resources/database.local.properties

HOST=$(docker inspect --format '{{ .NetworkSettings.IPAddress }}' nobt-io-postgres-db)
PORT='5432'
USERNAME='postgres'
PASSWORD='password'
NAME='postgres'

rm ${CONFIG_FILE}

echo "host=$HOST" >> ${CONFIG_FILE}
echo "port=$PORT" >> ${CONFIG_FILE}
echo "username=$USERNAME" >> ${CONFIG_FILE}
echo "password=$PASSWORD" >> ${CONFIG_FILE}
echo "name=$NAME" >> ${CONFIG_FILE}

# make git ignore changes to the properties file
git update-index --assume-unchanged ${CONFIG_FILE}