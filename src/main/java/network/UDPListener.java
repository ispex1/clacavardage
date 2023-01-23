package network;

import controller.UserController;
import javafx.application.Platform;
import view.MainFrame;

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
    private static DatagramSocket socket;
    private int port;
    private MainFrame frame;

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

    // ** GETTERS AND SETTERS **

    // Closing the socket
    public void closeSocket(){
        socket.close();
        setRunningState(false);
    }
    // Running state
    public void setRunningState(boolean state){
        this.isRunning = state;
    }
    // Port
    public void setPort(int port){
        this.port = port;
    }
    public void setFrame(MainFrame frame){
        this.frame = frame;
    }
}
