#!/bin/bash

mvn clean
mvn package
docker build -t image-server:tomcat .
docker run -d -p 9900:8080 image-server:tomcat
