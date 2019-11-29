package message;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import static constant.Constants.BUFFER_SIZE;

public class MessageOperator {

    public static byte[] serializeMessage(Message message) {
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        ObjectOutput oo = null;

        try {
            oo.writeObject(message);
            oo.close();
            oo = new ObjectOutputStream(bStream);
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
    //  when use docker, don't need sendPort, all nodes use the same PORT
    public static void sendMessage(DatagramSocket socket, String nodeName, Message message, int sendPort) {
        byte buf[] = null;
        buf = serializeMessage(message);

        try {
            InetAddress ip = InetAddress.getByName(nodeName);
            DatagramPacket DpSend = new DatagramPacket(buf, buf.length, ip, sendPort);
            socket.send(DpSend);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Message receiveMessage(DatagramSocket socket) {
        byte[] receive = new byte[BUFFER_SIZE];
        DatagramPacket DpReceive = null;

        // create a DatgramPacket to receive the data.
        DpReceive = new DatagramPacket(receive, receive.length);

        // revieve the data in byte buffer.
        try {
            socket.receive(DpReceive);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // deserialize from byte to message
        Message message = deserializeMessage(receive);

        // Clear the buffer after every message.
        // receive = new byte[BUFFER_SIZE];
        return message;
    }
}
