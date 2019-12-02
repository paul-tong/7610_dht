#dockerfile for client
FROM ubuntu:latest

MAINTAINER  Author PengTong <tong.p@husky.neu.edu>

FROM openjdk:8

# create src dir in docker, copy files under src folder into docker
# note: dockerfile should be in the same directory as current src folder
RUN mkdir /src
COPY ./src/ /src/

# cd to src folder
WORKDIR /src

# complie all source codes to get .class files
RUN javac constant/*.java message/*.java node/*.java hash/*.java

# entry point is the main class
ENTRYPOINT ["java","node/Client"]
