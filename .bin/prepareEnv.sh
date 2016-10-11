#!/usr/bin/env bash

[ -z "$1" ] && echo "App ID not given." && exit 1

cf set-env \
nobt-io \
DATABASE_CONNECTION_STRING \
$(cf curl /v2/apps/$1/env | .bin/jq '.system_env_json.VCAP_SERVICES.elephantsql[0].credentials.uri' | cut -d "\"" -f 2)