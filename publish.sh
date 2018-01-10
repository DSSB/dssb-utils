#!/bin/bash
gradle clean build publishToMavenLocal

PROJECTREPO=~/.m2/repository/dssb-utils
PUBLISHREPO=../maven-repository

if [ -d $PUBLISHREPO ]; then
    cp -Rf $PROJECTREPO $PUBLISHREPO
else
    (>&2 echo "Publish repositoy does not exist!")
fi
