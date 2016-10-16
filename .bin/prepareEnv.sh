#!/usr/bin/env sh

[ -z "$1" ] && echo "App ID not given." && exit 1

cf set-env \
nobt-io \
DATABASE_CONNECTION_STRING \
$(cf curl /v2/apps/$1/env | jq '.system_env_json.VCAP_SERVICES.elephantsql[0].credentials.uri' | cut -d "\"" -f 2)

# this script is only run on gitlab CI through .gitlab-ci.yml which means we can rely on global variables (CI_BUILD_REF)
cf set-env \
nobt-io \
SENTRY_RELEASE \
${CI_BUILD_REF}