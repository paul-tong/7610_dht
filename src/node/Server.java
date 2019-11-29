package node;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import constant.Constants;
import message.*;
import utils.Helper;

import static constant.Constants.*;

public class Server {
    private Node current;
    private Node next;
    private Node prev;
    private Node head;
    private Map<Integer, Integer> dataMap;
    private DatagramSocket socket;

    // todo: compute id base on name with hash function
    public Server(String name, int id, String headName, int headId) {
        current = new Node(name);
        current.setId(id);

        head = new Node(headName);
        head.setId(headId);

        next = null;
        prev = null;


        dataMap = new HashMap<>();

        try {
            // todo: for test use id as port number
            //  when use docker, use Constant.PORT
            socket = new DatagramSocket(id);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        System.out.println("server " + current.getName() + " started...");

        // start timers (timers will be in separate threads)
        //startStablizationTimer();

        // run separate threads for receiving message
        final Thread receiveThread = new Thread() {
            @Override
            public void run() {
                receiveMessage();
            }
        };
        receiveThread.setDaemon(true);
        receiveThread.start();

        // join current node to the ring
        join();

        // keep running application until killing it
        while(true) {
        }
    }

    // join a node to the ring
    private void join() {
        System.out.println("try to join the ring");

        // current node is head (first node), set it connects to itself
        if (current.getName().equals(head.getName())) {
            System.out.println("current node is head, connect to itself");
            next = current;
            prev = current;
            return;
        }

        // send message to head to find next node
        Message message = new FindNextMessage(new Node(current.getName(), current.getId()));
        MessageOperator.sendMessage(socket, head.getName(), message, head.getId());
    }


    private void receiveMessage() {
        while (true) {
            Message message = MessageOperator.receiveMessage(socket);
            int type = message.getType();

            if (type == FIND_NEXT_MESSAGE_TYPE) {
                handleFindNextMessage((FindNextMessage)message);
            }
            if (type == SET_NEXT_MESSAGE_TYPE) {
                handleSetNextMessage((SetNextMessage)message);
            }
            if (type == SET_PREV_MESSAGE_TYPE) {
                handleSetPrevMessage((SetPrevMessage)message);
            }
        }
    }

    private void handleSetNextMessage(SetNextMessage message) {
        Node nextNode = message.getNext();
        next = new Node(nextNode.getName(), nextNode.getId());

        System.out.println("set next node to " + next.getName() + "-" + next.getId());
    }

    private void handleSetPrevMessage(SetPrevMessage message) {
        Node prevNode = message.getPrev();
        prev = new Node(prevNode.getName(), prevNode.getId());

        System.out.println("set prev node to " + prev.getName() + "-" + prev.getId());
    }

    private void handleFindNextMessage(FindNextMessage message) {
        Node requestNode = message.getRequestNode();
        Node minBiggerNode = message.getMinBiggerNode();
        Node minNode = message.getMinNode();

        // arrive header again(iterate all nodes in the ring)
        if (current.getId() == head.getId() && !message.getIsFirst()) {
            System.out.println("back to the head node");

            /**
             * has node with bigger id than request node
             * connect request node to the minimal node that has bigger id
             */
            if (message.getMinBiggerNode() != null) {
                System.out.println("connect request node to minBigger node");

                // send message to request node to set minBigger node as next node
                Message setNextMessage = new SetNextMessage(new Node(minBiggerNode.getName(), minBiggerNode.getId()));
                MessageOperator.sendMessage(socket, requestNode.getName(), setNextMessage, requestNode.getId());


                // send message to minBigger node to set request node as previous node
                Message setPrevMessage = new SetPrevMessage(new Node(requestNode.getName(), requestNode.getId()));
                MessageOperator.sendMessage(socket, minBiggerNode.getName(), setPrevMessage, minBiggerNode.getId());
            }
            else { // has no bigger node, connect request node to the minimal node
                System.out.println("connect request node to min node");

                // send message to request node to set min node as next node
                Message setNextMessage = new SetNextMessage(new Node(minNode.getName(), minNode.getId()));
                MessageOperator.sendMessage(socket, requestNode.getName(), setNextMessage, requestNode.getId());

                // send message to min node to set request node as previous node
                Message setPrevMessage = new SetPrevMessage(new Node(requestNode.getName(), requestNode.getId()));
                MessageOperator.sendMessage(socket, minNode.getName(), setPrevMessage, minNode.getId());
            }

            return;
        }

        // haven't iterate the whole ring, update message information
        System.out.println("haven't iterate the whole ring, go to next node");

        int currentId = current.getId();
        String currentName = current.getName();

        message.setIsFirst(false);

        // find a overall minimal node
        if (minNode == null || currentId < minNode.getId()) {
            message.setMinNode(new Node(currentName, currentId));
        }

        // find a smaller node that has bigger id than request node
        if (currentId > requestNode.getId()
                && (minBiggerNode == null || currentId < minBiggerNode.getId())) {
            message.setMinBiggerNode(new Node(currentName, currentId));
        }

        // send message to next node
        MessageOperator.sendMessage(socket, next.getName(), message, next.getId());
    }

    public static void main(String[] args) {
        // todo: read names from args, compute id with hash function
        String nodeName = "node3";

        String headName = "node1";
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            int nodeId = Helper.getHash(inetAddress.toString() + ":" + Constants.PORT);
            InetAddress headIP = InetAddress.getByName(headName);
            int headId = Helper.getHash(headIP.toString() + ":" + Constants.PORT);
            final Server server = new Server(nodeName, nodeId, headName, headId);
            server.start();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

    }
}
