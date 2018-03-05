#!/bin/bash

docker network create -d bridge --subnet 192.168.0.0/24 --gateway 192.168.0.1 dockernet

docker build -t image-server .
docker run -d -p 443:443 image-server
