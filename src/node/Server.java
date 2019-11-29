package node;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

import message.FindNextMessage;

public class Server {
    private Node current;
    private Node next;
    private Node prev;
    private Node head;
    private Map<Integer, Integer> dataMap;
    //private int port = 9988;
    //private int sendPort = 9988;
    private DatagramSocket socket;

    public Server(int id, String name, String headName) {
        current = new Node(name);
        current.id = id;

        next = null;
        prev = null;
        head = new Node(headName);

        dataMap = new HashMap<>();

        try {
            // todo: for test use id as port number
            //  when use docker, use Constant.PORT
            socket = new DatagramSocket(id);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    // join a node to the ring
    private void join() {
        // current node is head (first node), set it connects to itself
        if (current.name.equals(head.name)) {
            next = current;
            prev = current;
            return;
        }

        // send message to head to find next node
        FindNextMessage message = new FindNextMessage(current);
        sendFindNextMessage(socket, message, head);
    }


    private void receiveMessage() {
        // recieve a message for finding sucessor
        if (id > newId) {
            // set previous pointer to new node
            // send message to the new node, set new node's successor to it
        }
    }

}
