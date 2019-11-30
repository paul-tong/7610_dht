package node;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import message.*;
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
        current = new Node(name, id);

        head = new Node(headName, headId);

        // connect to itself initially
        next = current;
        prev = current;


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
        startStabilizationTimer();

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

    /**
     * join a node to the ring
     */
    private void join() {
        System.out.println("try to join the ring");

        // current node is head (first node), set it connects to itself
        if (current.getName().equals(head.getName())) {
            System.out.println("current node is head, remain connect to itself");
            return;
        }

        // send message to head to find next node
        Message message = new FindNextMessage(new Node(current.getName(), current.getId()));
        MessageOperator.sendMessage(socket, head.getName(), message, head.getId());
    }


    private void startStabilizationTimer() {
        TimerTask repeatedTask = new TimerTask() {
            public void run() {
                sendStabilizationMessage();
            }
        };

        // timer to repeat at certain interval
        Timer stabilizationTimer = new Timer("stabilizationTimer");
        stabilizationTimer.scheduleAtFixedRate(repeatedTask, STABLIZATION_DELAY, STABLIZATION_INTERVAL);

    }

    private void sendStabilizationMessage() {
        // send message to request its successor's predecessor
        Message message = new RequestPrevMessage(new Node(current.getName(), current.getId()));
        MessageOperator.sendMessage(socket, next.getName(), message, next.getId());
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
            if (type == REQUEST_PREV_MESSAGE_TYPE) {
                handleRequestPrevMessage((RequestPrevMessage)message);
            }
            if (type == RETURN_PREV_MESSAGE_TYPE) {
                handleReturnPrevMessage((ReturnPrevMessage)message);
            }
            if (type == NOTIFY_NEXT_MESSAGE_TYPE) {
                handleNotifyNextMessage((NotifyNextMessage)message);
            }
        }
    }


    /**
     * return current node's prev node to request node
     * @param message
     */
    private void handleRequestPrevMessage(RequestPrevMessage message) {
        // send message to return current node's prev node to request node
        Node requestNode = message.getRequestNode();
        Message messageReturn = new ReturnPrevMessage(new Node(prev.getName(), prev.getId()));
        MessageOperator.sendMessage(socket, requestNode.getName(), messageReturn, requestNode.getId());
    }


    /**
     * receive the prev node of its next node, check if need to update
     * current node's next node
     * @param message
     */
    private void handleReturnPrevMessage(ReturnPrevMessage message) {
        Node pn = message.getPreNode(); // potential next node

        int currentId = current.getId();
        int nextId = next.getId();
        int pnId = pn.getId();

        // is it self, do nothing
        if (pnId == currentId) {
            //System.out.println("next.prev is itself, connection is stable");
            return;
        }

        /**
         * pnId > currentId && pnId < nextId means insert a node in between two nodes
         * currentId == nextId, means current node connects to itself, we need to
         *  break the self circle
         * currentId > nextId, means current node is the end of the ring, we may
         *  insert a biggest or smallest node at the end of ring
         */
        if ((pnId > currentId && pnId < nextId) || (currentId >= nextId)) {
            next = new Node(pn.getName(), pnId); // set new next node
            System.out.println("set new next node: " + next.getName() + "-" + next.getId());
        }

        // notify current node's next node the existence of current node
        Message messageNotify = new NotifyNextMessage(new Node(current.getName(), current.getId()));
        MessageOperator.sendMessage(socket, next.getName(), messageNotify, next.getId());
    }


    /**
     * receive notification of a potential previous node
     * @param message
     */
    private void handleNotifyNextMessage(NotifyNextMessage message) {
        Node pn = message.getCurNode(); // potential previous node

        int currentId = current.getId();
        int prevId = prev.getId();
        int pnId = pn.getId();

        /**
         * pnId > prevId && pnId < currentId means insert a node between two nodes
         * currentId == prevId means current node connects to itself
         * todo: needs consider currentId < prevId?
         */
        if ((pnId > prevId && pnId < currentId) || (currentId == prevId)) {
            prev = new Node(pn.getName(), pnId);
            System.out.println("set new prev node: " + prev.getName() + "-" + prev.getId());
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
            //System.out.println("back to the head node");

            /**
             * has node with bigger id than request node
             * connect request node to the minimal node that has bigger id
             */
            if (message.getMinBiggerNode() != null) {
                //System.out.println("connect request node to minBigger node");

                // send message to request node to set minBigger node as next node
                Message setNextMessage = new SetNextMessage(new Node(minBiggerNode.getName(), minBiggerNode.getId()));
                MessageOperator.sendMessage(socket, requestNode.getName(), setNextMessage, requestNode.getId());


                // send message to minBigger node to set request node as previous node
                Message setPrevMessage = new SetPrevMessage(new Node(requestNode.getName(), requestNode.getId()));
                MessageOperator.sendMessage(socket, minBiggerNode.getName(), setPrevMessage, minBiggerNode.getId());
            }
            else { // has no bigger node, connect request node to the minimal node
                //System.out.println("connect request node to min node");

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
        //System.out.println("haven't iterate the whole ring, go to next node");

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
        // todo: read names from args, get ip address from name, compute id by hashing ip
        //  for testing, all nodes use localhost with different ports
        //  so we can run multiple instances on intellij
        String nodeName = "node2";
        int nodeId = 9982;

        String headName = "node1";
        int headId = 9981;

        final Server server = new Server(nodeName, nodeId, headName, headId);
        server.start();

        // todo: uncommand this when using docker
        /*
        try {
            String nodeIp = InetAddress.getByName(nodeName).toString() + ":" + PORT;
            String headIp = InetAddress.getByName(headName).toString() + ":" + PORT;
            int nodeId = HashGenerator.getHash(nodeIp);
            int headId = HashGenerator.getHash(headIp);

            final Server server = new Server(nodeName, nodeId, headName, headId);
            server.start();

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }*/
    }
}
