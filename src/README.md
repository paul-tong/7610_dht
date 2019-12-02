# Introduction

This is the implementation of basic Chord application, including data lookup, node join and leave, stabilization.

## How to run

To run the program, we need run the client and server separately.

The client is for user to input data and do the data lookup and control node leave.

The server is the Chord server to maintain nodes to store data.

Build the program using Docker

 Client: `docker build -f docker/client/Dockerfile . -t client`
 
 Server: `docker build -f docker/server/Dockerfile . -t server`
 
 Before we run the client, we need to make sure at least one node available, 
 since the client will use that node as the entry point of the Chord. 
 
 To Run the first node, we assume the first node named `node1`,
 
 `docker run --name node1 -h node1 --network mynetwork server -h node1`
 
 Then to run the client, use 
 `docker run -i -t --name client -h client --network mynetwork client -h client`
 
 To add a new node and join the Chord, use
 `docker run --name node2 -h node2 --network mynetwork server -h node2`
 
 
we can replace `node2` to other names to create other new nodes.

## Client operations

The client supports four operations:

1. put (eg. put 2 5    (means add key value pair <2, 5> to table))

2. get (eg. get 2      (means get value with key 2 from table))

3. remove (eg. remove 2   (means remove pair with key 2 from table))

4. leave (eg. leave node2 (means leave node2 from ring))

The server will receive messages and run the functionality.
