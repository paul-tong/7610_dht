# Dockerfile

FROM  phusion/baseimage:0.9.17


FROM openjdk:9
RUN mkdir /src
COPY ./src/ /src
COPY hosts /src

WORKDIR /src
RUN javac **/*.java

ENTRYPOINT ["java","node/Server"]