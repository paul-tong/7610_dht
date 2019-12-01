package node;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Scanner;

import constant.Constants;
import message.Message;
import message.MessageOperator;
import message.RequestNodeLeaveMessage;
import message.RespondClientMessage;

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
            socket = new DatagramSocket(id);
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
            switch(action) {
                case "leave":
                    // todo: for test, need to input leave node id(port), don't need if in docker
                    String leaveNodeName = inputs[1];
                    int leaveNodeId = Integer.parseInt(inputs[2]);

                    Message message = new RequestNodeLeaveMessage(new Node(client.getName(), client.getId()));
                    MessageOperator.sendMessage(socket, leaveNodeName, message, leaveNodeId);
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
            if (type == Constants.RESPOND_CLIENT_MESSAGE_TYPE) {
                RespondClientMessage response = (RespondClientMessage)message;
                System.out.println(response.getNote());
            }
        }
    }


    public static void main(String[] args) {
        // todo: read names from args, get ip address from name, compute id by hashing ip
        //  for testing, all nodes use localhost with different ports
        //  so we can run multiple instances on intellij
        String clientName = "client";
        int clientId = 9980;

        String headName = "node1";
        int headId = 9981;

        final Client client = new Client(clientName, clientId, headName, headId);
        client.start();
    }
}
