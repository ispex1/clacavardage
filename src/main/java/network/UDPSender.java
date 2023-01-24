package network;

import java.io.IOException;
import java.net.*;

/**
 * UDPSender class is used to send UDP packets
 * We can send messages to a specific port or in broadcast
 */
public class UDPSender {

    /**
     * UDPSender constructor
     */
    public UDPSender() {
    }

    /**
     * It send a UDP packet to a specific address on a specific port
     * @param message , string message converted to DatagramPacket
     * @param port , port to send the message
     */
    public static void sendUDP(String message, int port, String ipString) {
        InetAddress address;
        try {
            address = InetAddress.getByName(ipString);
        } catch (UnknownHostException e) {
            address = null;
            System.out.println("Address recuperation error; address is set to null");
            e.printStackTrace();
        }

        DatagramSocket socket;
        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            socket = null;
            System.out.println("Socket creation error; socket is set to null");
            e.printStackTrace();
        }
        
        byte[] buffer = message.getBytes();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, port);
        try {
            assert socket != null;
            socket.send(packet);
        } catch (IOException e) {
            System.out.println("Packet sending error");
            e.printStackTrace();
        }
        socket.close();
    }

    /**
     * It send a UDP packet in broadcast
     * @param message String message converted to DatagramPacket
     */
    public static void sendBroadcast(String message, int port) {
        DatagramSocket socket;
        try {
            socket = new DatagramSocket();
            socket.setBroadcast(true);
        } catch (SocketException e1) {
            socket = null;
            System.out.println("Socket creation error; socket is set to null");
            e1.printStackTrace();
        }
        byte[] buffer = message.getBytes();

        // Send a packet to the broadcast address
        DatagramPacket packet;
        try {
            packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName("255.255.255.255"), port);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Sending packet to broadcast : " + message );
            try {
                assert socket != null;
                socket.send(packet);
            } catch (IOException e) {
                System.out.println("Packet sending error");
                e.printStackTrace();
            }
        socket.close();
    }
}
