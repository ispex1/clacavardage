package network;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Objects;

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
     * sendUDP method is used to send a UDP packet to a specific address on a specific port
     * @param message String message converted to DatagramPacket
     * @param port
     * @throws IOException
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
            socket.send(packet);
        } catch (IOException e) {
            System.out.println("Packet sending error");
            e.printStackTrace();
        }
        socket.close();
    }


    /**
     * sendBroadcast method is used to send a UDP packet in broadcast
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
        for (InetAddress address : getBroadcastAddresses()) {
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, port);
            try {
                socket.send(packet);
            } catch (IOException e) {
                System.out.println("Packet sending error");
                e.printStackTrace();
            }
        }
        socket.close();
    }

    /**
     * getBroadcastAddresses get every addresses of the network and put them in an array
     * @return array of addresses
     *
     */
    private static ArrayList<InetAddress> getBroadcastAddresses() {
        ArrayList<InetAddress> broadcastList = new ArrayList<InetAddress>();
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();

                if (networkInterface.isLoopback() || !networkInterface.isUp()) {
                    continue;
                }

                networkInterface.getInterfaceAddresses().stream()
                        .map(a -> a.getBroadcast())
                        .filter(Objects::nonNull)
                        .forEach(broadcastList::add);
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return broadcastList;
    }

    public static ArrayList<InetAddress> getBroadcastAddresses2() {
        ArrayList <InetAddress> addresses = new ArrayList<InetAddress>();
        Enumeration<NetworkInterface> interfaces;
        try {
            interfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            interfaces = null;
            System.out.println("Network interface recuperation error; interfaces is set to null");
            e.printStackTrace();
        }
        for (NetworkInterface networkInterface : Collections.list(interfaces)) {
            addresses.add(networkInterface.getInetAddresses().nextElement());
        }
        return addresses;
    }


}
