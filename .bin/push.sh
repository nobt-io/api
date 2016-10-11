#!/usr/bin/env bash

[ -z "$1" ] && echo "Route not given." && exit 1

cf push -n $1 -p rest-api/build/distributions/*.zip -f rest-api/manifest.yml