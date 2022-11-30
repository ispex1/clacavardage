package network;

import java.net.DatagramSocket;
import java.net.SocketException;

public class UDPListener extends Thread {
    
    // ** ATTRIBUTES **
    private int situation;
    // situation = 0 -> not listening
    // situation = 1 -> listening
    // situation = 2 -> trying to connect, check for broadcast validation of the pseudo
    // situation = 3 -> trying to change the pseudo
    // situation = 4 -> notifying the network of the state
    private boolean isRunning;
    private boolean pseudoValid;
    private DatagramSocket socket;
    private int port;

    // ** CONSTRUCTOR **
    public UDPListener(int situation){
        this.situation = situation;
        start();
    }

    

    // ** METHODS **
    //connexion broadcast validation
    private void connexionBroadcastValidation(){

    }

    //pseudo changing validation
    private void pseudoChanging(){

    }

    //listening UDP messages
    private void listenUDP(){

    }

    //notifying the network of the state
    private void notifyNetwork(){

    }

    //run method
    public void run(){
        setRunningState(true);
        setPort(1234);
        
        try {
            socket = new DatagramSocket(port);
       
            while (isRunning){
                switch(situation){
                    case 1:
                        //listening
                        listenUDP();
                        break;
                    case 2:
                        //trying to connect, check for broadcast validation of the pseudo
                        connexionBroadcastValidation();
                        break;
                    case 3:
                        //trying to change the pseudo
                        pseudoChanging();
                        break;
                    case 4:
                        //notifying the network of the state
                        notifyNetwork();
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
