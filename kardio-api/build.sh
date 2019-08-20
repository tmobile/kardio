#!/bin/sh

MAVEN_OPTS="$MAVEN_OPTS -DforkMode=always"
mvn $MAVEN_OPTS  clean package

