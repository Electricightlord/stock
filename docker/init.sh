#!/bin/bash
set -e
mvn -f ../pom.xml install
cp ../target/stock-0.0.1-SNAPSHOT.jar ./java/stock-0.0.1-SNAPSHOT.jar
docker build -t mysql:v1.0 -f ./mysql/Dockerfile ./mysql
docker build -t redis:v1.0 -f ./redis/Dockerfile ./redis
docker build -t java:v1.0 -f ./java/Dockerfile ./java
docker-compose up