#!/bin/bash

docker network create -d bridge --subnet 192.168.0.0/24 --gateway 192.168.0.1 dockernet

docker build -t doorman .
docker run -p 80:80 doorman

cd plantuml-server
docker build -t plantuml-server .
docker run -p 9900:8182 plantuml-server

cd activiti_image_server
mvn package
docker build -t activiti_image_server .
docker run -p 9901:8080 activiti_image_server
