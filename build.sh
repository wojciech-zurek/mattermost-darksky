#!/usr/bin/env sh

./gradlew clean bootJar
docker build -t eu.wojciechzurek/mattermost-darksky:0.0.1 -t eu.wojciechzurek/mattermost-darksky:latest -f Dockerfile .