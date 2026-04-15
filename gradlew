#!/usr/bin/env sh
APP_GRADLEW="./app/gradlew"
if [ -f "$APP_GRADLEW" ]; then
    exec sh "$APP_GRADLEW" "$@"
else
    echo "Error: gradlew not found in app folder"
    exit 1
fi
