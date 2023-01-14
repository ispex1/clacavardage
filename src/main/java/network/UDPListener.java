package network;

import controller.UserController;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;


/**
 * UDPListener class is used to listen for UDP packets on a given port,
 * we can listen for connexion responses, changing pseudo response
 */
public class UDPListener extends Thread {

    // ** ATTRIBUTES **
    private boolean isRunning;
    private boolean pseudoValid;
    private static DatagramSocket socket;
    private DatagramPacket receivePacket;
    private int port;

    // ** CONSTRUCTOR **
    public UDPListener(int port){
        setPort(port);
    }

    //run method
    public void run(){
        setRunningState(true);

        byte[] buffer = new byte[1000];

        try {
            socket = new DatagramSocket(port);

            while (isRunning){
                receivePacket = new DatagramPacket(buffer, buffer.length);
                try {
                    socket.receive(receivePacket);
                    String data = new String(receivePacket.getData(), 0, receivePacket.getLength());
                    UserController.informationTreatment(data);

                    //System.out.println(data);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }



    // ** GETTERS AND SETTERS **

    // Closing the socket
    public void closeSocket(){
        socket.close();
    }
    // Running state
    public boolean getRunningState(){
        return isRunning;
    }
    public void setRunningState(boolean state){
        this.isRunning = state;
    }
    // Pseudo
    public boolean getPseudoValid(){
        return pseudoValid;
    }
    public void setPseudoValid(boolean isValid){
        this.pseudoValid = isValid;
    }
    // Port
    public int getPort(){
        return port;
    }
    public void setPort(int port){
        this.port = port;
    }

}
