#!/bin/bash

TOMCAT=apache-tomcat-8.0.30
TOMCAT_WEBAPPS=$TOMCAT/webapps
TOMCAT_CONFIG=$TOMCAT/conf/server.xml
TOMCAT_ARCHIVE=$TOMCAT.tar.gz
TOMCAT_URL=http://www.eu.apache.org/dist/tomcat/tomcat-8/v8.0.30/bin/$TOMCAT_ARCHIVE

curl -O $TOMCAT_URL

tar -zxf $TOMCAT_ARCHIVE
rm $TOMCAT_ARCHIVE


# place tomcat customizations here
sed -i.bak s/8080/11555/ $TOMCAT_CONFIG 

