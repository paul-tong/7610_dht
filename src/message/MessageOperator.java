package message;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import static constant.Constants.BUFFER_SIZE;
import static constant.Constants.PORT;

public class MessageOperator {

    public static byte[] serializeMessage(Message message) {
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        try {
            ObjectOutput oo = new ObjectOutputStream(bStream);
            oo.writeObject(message);
            oo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bStream.toByteArray();
    }

    public static Message deserializeMessage(byte[] messageBytes) {
        Message message = null;
        ObjectInputStream iStream = null;

        try {
            iStream = new ObjectInputStream(new ByteArrayInputStream(messageBytes));
            Object o = iStream.readObject();
            // System.out.println(o.getClass());
            message = (Message)o;
            iStream.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return message;
    }

    // todo: currently nodeName is always localhost, sendPort is id of target node
    //  when use docker, don't need sendPort, all nodes use the same PORT -> sendMessage(socket, nodeName, message)
    public static void sendMessage(DatagramSocket socket, String nodeName, Message message, int sendPort) {
        //System.out.println("send message type " + message.getType() + " to: " + nodeName);

        byte buf[] = null;
        buf = serializeMessage(message);

        try {
            // todo: use nodeName instead of localhost, don't need sendPort
            InetAddress ip = InetAddress.getByName(nodeName);
            //InetAddress ip = InetAddress.getByName("localhost");
            DatagramPacket DpSend = new DatagramPacket(buf, buf.length, ip, PORT);
            socket.send(DpSend);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Message receiveMessage(DatagramSocket socket) {
        byte[] receive = new byte[BUFFER_SIZE];
        DatagramPacket dpReceive = null;

        // create a DatgramPacket to receive the data.
        dpReceive = new DatagramPacket(receive, receive.length);

        // revieve the data in byte buffer.
        try {
            socket.receive(dpReceive);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // deserialize from byte to message
        Message message = deserializeMessage(receive);

        // Clear the buffer after every message.
        // receive = new byte[BUFFER_SIZE];

        //System.out.println("receive message type " + message.getType());
        return message;
    }
}
