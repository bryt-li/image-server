#!/bin/bash

docker build -t plantuml-server .
docker run -d -p 9900:8182 plantuml-server
