package node;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

import static constant.Constants.*;
import hash.HashGenerator;
import message.*;

/**
 * Listen to data add, delete, update, lookup operations from users
 */
public class Client {
    private Node client;
    private Node head;
    private DatagramSocket socket;

    // todo: compute id base on name with hash function
    public Client(String name, int id, String headName, int headId) {
        client = new Node(name, id);

        head = new Node(headName, headId);

        try {
            // todo: for test use id as port number
            //  when use docker, use Constant.PORT
            socket = new DatagramSocket(PORT);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        System.out.println("client " + client.getName() + " started...");

        // run separate threads for receiving user input
        final Thread inputThread = new Thread() {
            @Override
            public void run() {
                listenUserInput();
            }
        };
        inputThread.setDaemon(true);
        inputThread.start();

        // run separate threads for receiving message
        final Thread receiveThread = new Thread() {
            @Override
            public void run() {
                receiveMessage();
            }
        };
        receiveThread.setDaemon(true);
        receiveThread.start();

        // keep running application until killing it
        while(true) {
        }
    }

    private void listenUserInput() {
        while(true) {
            /**
             *  read each input line
             *  input format:
             *    1) node operation: action nodeName
             *       eg, leave node2 (means leave node2 from ring)
             *    2) data operation: action key val(optional)
             *       eg, put 2 5    (means add key value pair <2, 5> to table)
             *           get 2      (means get value with key 2 from table)
             *           remove 2   (means remove pair with key 2 from table)
             */
            Scanner in = new Scanner(System.in);
            String s = in.nextLine();

            String[] inputs = s.split(" ");
            if (inputs.length == 0) {
                System.out.println("invalid input");
                continue;
            }

            String action = inputs[0];
            String key = "";
            int keyId = -1;
            int val = -1;

            switch(action) {
                case "leave":
                    // todo: for test, need to input leave node id(port), don't need if in docker
                    String leaveNodeName = inputs[1];
                    int leaveNodeId = Integer.parseInt(inputs[2]); // don't need in docker

                    Message messageLeave = new RequestNodeLeaveMessage();
                    MessageOperator.sendMessage(socket, leaveNodeName, messageLeave, leaveNodeId);
                    break;
                case "put":
                    key = inputs[1];
                    val = Integer.parseInt(inputs[2]);
                    keyId = HashGenerator.getHash(key);

                    // send message to head to look up the node that this key belongs to
                    Message messagePut = new FindNextMessage(new Node("data", keyId), PUT_OPERATION_TYPE, keyId, val);
                    MessageOperator.sendMessage(socket, head.getName(), messagePut, head.getId());
                    break;
                case "get":
                    key = inputs[1];
                    keyId = HashGenerator.getHash(key);

                    // send message to head to look up the node that this key belongs to
                    Message messageGet = new FindNextMessage(new Node("data", keyId), GET_OPERATION_TYPE, keyId, -1);
                    MessageOperator.sendMessage(socket, head.getName(), messageGet, head.getId());
                    break;
                case "remove":
                    key = inputs[1];
                    keyId = HashGenerator.getHash(key);

                    // send message to head to look up the node that this key belongs to
                    Message messageRemove = new FindNextMessage(new Node("data", keyId), REMOVE_OPERATION_TYPE, keyId, -1);
                    MessageOperator.sendMessage(socket, head.getName(), messageRemove, head.getId());
                    break;
                default:
                    System.out.println("unknown action");
            }
        }
    }

    private void receiveMessage() {
        while(true) {
            Message message = MessageOperator.receiveMessage(socket);
            int type = message.getType();

            // print responded note
            if (type == RESPOND_CLIENT_MESSAGE_TYPE) {
                RespondClientMessage response = (RespondClientMessage)message;
                System.out.println(response.getNote());
            }
        }
    }


    public static void main(String[] args) {
        // todo: get ip address from name, compute id by hashing ip
        //  for testing, all nodes use localhost with different ports
        //  so we can run multiple instances on intellij
        //final Client client = new Client(CLIENT_NAME, CLIENT_ID, HEAD_NAME, HEAD_ID);
        //client.start();

        // todo: uncommand this when using docker
        try {
            String headName = HEAD_NAME;
            String headIp = InetAddress.getByName(headName).toString() + ":" + PORT;
            int headId = HashGenerator.getHash(headIp);

            final Client client = new Client(CLIENT_NAME, CLIENT_ID, headName, headId);
            client.start();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
