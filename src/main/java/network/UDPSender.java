package network;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;

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

    public void sendUDP(String message, int port, InetAddress address) throws IOException {
        DatagramSocket socket = new DatagramSocket();
        byte[] buffer = message.getBytes();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, port);
        socket.send(packet);
        socket.close();
    }


    /**
     * sendBroadcast method is used to send a UDP packet in broadcast
     * @param message String message converted to DatagramPacket
     */
    public void sendBroadcast(String message, int port) throws SocketException {
        DatagramSocket socket = new DatagramSocket();
        socket.setBroadcast(true);
        byte[] buffer = message.getBytes();
        for (InetAddress address : getBroadcastAddresses()) {
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, port);
            try {
                socket.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * getBroadcastAddresses get every addresses of the network and put them in an array
     * @return array of addresses
     *
     */

    public ArrayList<InetAddress> getBroadcastAddresses() throws SocketException {
        ArrayList <InetAddress> addresses = new ArrayList<InetAddress>();
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        for (NetworkInterface networkInterface : Collections.list(interfaces)) {
            addresses.add(networkInterface.getInetAddresses().nextElement());
            }
        return addresses;
    }


}
