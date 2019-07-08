#!/bin/bash
set -e
docker build -t mysql:v1.0 -f ./mysql/Dockerfile ./mysql
docker build -t redis:v1.0 -f ./redis/Dockerfile ./redis
docker build -t java:v1.0 -f ./java/Dockerfile ./java
docker-compose up