#!/bin/bash
pushd operationManager

# Shared code
mvn install -DskipTests

# georesolver
pushd operationManager-georesolver
mvn cf:push -DskipTests
popd

# newsbeeper
pushd operationManager-newsbeeper
mvn cf:push -DskipTests
popd

# web
pushd operationManager-web
mvn cf:push -DskipTests
popd

popd
