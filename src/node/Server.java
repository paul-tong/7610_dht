package node;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

import message.*;

import static constant.Constants.*;

public class Server {
    private Node current;
    private Node next;
    private Node prev;
    private Node head;
    private Map<Integer, Integer> dataMap;
    //private int port = 9988;
    //private int sendPort = 9988;
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
        // wait some time, so other nodes are started
        /*System.out.println("server " + current.getName() + " created, waiting...");
        try {
            TimeUnit.SECONDS.sleep(INITIAL_WAIT_TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

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
            System.out.println("current node is head");
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
                System.out.println("receive message: FIND_NEXT_MESSAGE_TYPE");
                handleFindNextMessage((FindNextMessage)message);
            }
            if (type == SET_NEXT_MESSAGE_TYPE) {
                System.out.println("receive message: SET_NEXT_MESSAGE_TYPE");
                handleSetNextMessage((SetNextMessage)message);
            }
            if (type == SET_PREV_MESSAGE_TYPE) {
                System.out.println("receive message: SET_PREV_MESSAGE_TYPE");
                handleSetPrevMessage((SetPrevMessage)message);
            }
        }
    }

    private void handleSetNextMessage(SetNextMessage message) {

    }

    private void handleSetPrevMessage(SetPrevMessage message) {

    }

    private void handleFindNextMessage(FindNextMessage message) {
        System.out.println("is first: " + message.getIsFirst() + " requestNode:" + message.getRequestNode().getName());

        // arrive header again(iterate all nodes in the ring)
        if (current.getId() == head.getId() && !message.getIsFirst()) {
            System.out.println("back to the head node");
            /**
             * has node with bigger id than request node
             * connect request node to the minimal node that has bigger id
             */
            if (message.getMinBiggerNode() != null) {
                // send message to request node to set next node

                // send message to minBigger node to set previous node
            }
            else { // has no bigger node, connect request node to the minimal node
                // send message to request node to set next node

                // send message to min node to set previous node
            }

            return;
        }

        // haven't iterate the whole ring, update message information
        int currentId = current.getId();
        String currentName = current.getName();

        message.setIsFirst(false);

        // find a overall minimal node
        if (message.getMinNode() == null || currentId < message.getMinNode().getId()) {
            message.setMinNode(new Node(currentName, currentId));
        }

        // find a smaller node that has bigger id than request node
        if (currentId > message.getRequestNode().getId()
                && ( message.getMinBiggerNode() == null || currentId < message.getMinBiggerNode().getId())) {
            message.setMinBiggerNode(new Node(currentName, currentId));
        }

        // send message to next node
        System.out.println("send findNextMessage to next node: " + next.getName());
        MessageOperator.sendMessage(socket, next.getName(), message, next.getId());
    }

    public static void main(String[] args) {
        // todo: read names from args, compute id with hash function
        String nodeName = "node2";
        int nodeId = 9987;

        String headName = "node1";
        int headId = 9986;

        final Server server = new Server(nodeName, nodeId, headName, headId);
        server.start();
    }
}
