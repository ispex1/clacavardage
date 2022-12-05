package network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.SocketException;
import java.nio.Buffer;


/**
 * UDPListener class is used to listen for UDP packets on a given port, 
 * we can listen for connexion responses, changing pseudo response
 */
public class UDPListener extends Thread {
    
    // ** ATTRIBUTES **
    private int situation;
    // situation = 0 -> not listening
    // situation = 1 -> listening
    // situation = 2 -> trying to connect, listen for broadcast validation of the pseudo
    // situation = 3 -> trying to change the pseudo, listen for broadcast validation of the pseudo
    private boolean isRunning;
    private boolean pseudoValid;
    private static DatagramSocket socket;
    private DatagramPacket receivePacket;
    private int port;

    // ** CONSTRUCTOR **
    public UDPListener(int situation, int port){
        setSituation(situation);
        setPort(port);
        start();
    }

    // ** METHODS **
    //connexion broadcast validation
    private void connexionBroadcastValidation(){
        //TODO 
    }

    //pseudo changing validation
    private void pseudoChanging(){
        //TODO
    }

    //listening UDP messages
    private void listenUDP(byte[] buffer){
        socket.close();
        try {
            socket = new DatagramSocket(port);
            receivePacket = new DatagramPacket(buffer, buffer.length);
            try {
                socket.receive(receivePacket);
                String data = new String(receivePacket.getData(), 0, receivePacket.getLength());
                //TODO Appel fonction a traiter en fonction du message recu dans le package model
                // Just printing the data for now
                System.out.println(data);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        
        
    }

    //run method
    public void run(){
        setRunningState(true);


        byte[] buffer = new byte[1000];
        
        try {
            socket = new DatagramSocket(port);
       
            while (isRunning){
                switch(getSituation()){
                    case 1:
                        //listening
                        listenUDP(buffer);
                        break;
                    case 2:
                        //trying to connect, check for broadcast validation of the pseudo
                        connexionBroadcastValidation();
                        break;
                    case 3:
                        //trying to change the pseudo
                        pseudoChanging();
                        break;
                    default:
                        break;
                }
            }
         } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }



    // ** GETTERS AND SETTERS **
    // Situation
    public int getSituation() {
        return situation;
    }
    public void setSituation(int situation) {
        this.situation = situation;
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
