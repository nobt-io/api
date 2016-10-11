#!/usr/bin/env bash

[ -z "$1" ] && echo "Space not given." && exit 1

cf api https://api.run.pivotal.io
cf auth $CF_BUILD_USER $CF_BUILD_USER_PASSWORD
cf target -o nobt.io -s $1