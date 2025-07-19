#!/usr/bin/env bash
# Publish to OSSRH Staging API.
./gradlew publish

# Transfer from OSSRH Staging API to Central Publisher Portal.
BEARER=$(printf "$ORG_GRADLE_PROJECT_portalUsername:$ORG_GRADLE_PROJECT_portalPassword" | base64)
curl --request POST \
    --include \
    --header "Authorization: Bearer $BEARER" \
    https://ossrh-staging-api.central.sonatype.com/manual/upload/defaultRepository/io.github.mikewacker
