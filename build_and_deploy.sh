#!/bin/bash
pushd operationManager

# Clean shared jar
pushd operationManager-shared
mvn clean
popd

# Build code, run test and install shared component
mvn install || { echo -e "\e[1mFailure at install not deploying." ; tput sgr0 ; exit 1; }

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
