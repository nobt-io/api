#!/usr/bin/env sh

cf create-service elephantsql turtle database # Make sure the database service exists. Doesn't error if it already exists.

cf set-env \
nobt-io \
DATABASE_CONNECTION_STRING \
$(cf get-env nobt-io ".system_env_json.VCAP_SERVICES.elephantsql[0].credentials.uri" | cut -f 1)

# this script is only run on gitlab CI through .gitlab-ci.yml which means we can rely on global variables (CI_BUILD_REF)
cf set-env \
nobt-io \
SENTRY_RELEASE \
${CI_BUILD_REF}