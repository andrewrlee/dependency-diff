#!/bin/bash

DIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )

TEST_RESOURCE_DIR=$DIR/../src/test/resources/
VERSION_1_ROOT=$DIR/test-0.0.1 
VERSION_2_ROOT=$DIR/test-0.0.2 

echo "Copying jars to ${TEST_RESOURCE_DIR}"
rm $TEST_RESOURCE_DIR/test-0.0.1-SNAPSHOT.jar
mvn -f $VERSION_1_ROOT/pom.xml clean install
cp $VERSION_1_ROOT/target/test-0.0.1-SNAPSHOT.jar $TEST_RESOURCE_DIR


rm $TEST_RESOURCE_DIR/test-0.0.2-SNAPSHOT.jar
mvn -f $VERSION_2_ROOT/pom.xml clean install
cp $VERSION_2_ROOT/target/test-0.0.2-SNAPSHOT.jar $TEST_RESOURCE_DIR


