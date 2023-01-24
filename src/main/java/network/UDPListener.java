package network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import controller.UserController;
import view.MainFrame;

import javafx.application.Platform;

/**
 * UDPListener class is used to listen for UDP incoming packets on a given port,
 * we can listen for connexion responses, changing pseudo response
 */
public class UDPListener extends Thread {
    private boolean isRunning; // Boolean to know if the thread is running
    private static DatagramSocket socket; // Datagram socket
    private int port; // Port to listen on
    private MainFrame frame; // Main frame of the application

    /**
     * Constructor of the UDPListener class
     * @param port , the port to listen to
     */
    public UDPListener(int port){
        setPort(port);
    }

    /**
     * This method is called when the thread is started
     * It listens for incoming UDP packets
     * When a packet is received, it calls the method to handle the packet
     * It also updates the chat pane
     *
     */
    public void run(){
        setRunningState(true);

        byte[] buffer = new byte[1000];

        try {
            socket = new DatagramSocket(port);

            while (isRunning){
                DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
                try {
                    socket.receive(receivePacket);
                    String data = new String(receivePacket.getData(), 0, receivePacket.getLength());
                    System.out.println("Brut Data Receive : " + data);

                    UserController.informationTreatment(data);
                    if (this.frame != null){
                        Platform.runLater(() -> {
                            frame.updateUsersList();
                            if(frame.getChatter()!=null){
                                try {
                                    frame.updateChatPane();
                                    frame.updateSelection();
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            else frame.hidePane();
                        });
                    }
                } catch (IOException e) {
                    Platform.exit();
                    System.exit(0);
                    e.printStackTrace();
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is used to close the UDP Listener
     * It closes the socket
     * It sets the running state to false
     */
    public void closeSocket(){
        socket.close();
        setRunningState(false);
    }

    /**
     * Setter for the running state boolean
     * @param state , the boolean to set
     */
    public void setRunningState(boolean state){
        this.isRunning = state;
    }

    /**
     * Setter for the port
     * @param port , the port to set
     */
    public void setPort(int port){
        this.port = port;
    }

    /**
     * Setter for the main frame
     * @param frame , the main frame to set
     */
    public void setFrame(MainFrame frame){
        this.frame = frame;
    }
}
