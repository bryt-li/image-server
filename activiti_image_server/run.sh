#!/bin/bash

mvn package
docker build -t activiti_image_server .
docker run -d -p 9901:8080 activiti_image_server
